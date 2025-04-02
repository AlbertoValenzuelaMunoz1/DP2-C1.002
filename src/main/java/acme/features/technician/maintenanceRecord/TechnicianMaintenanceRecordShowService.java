
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.Status;
import acme.entities.group.aircraft.Aircraft;
import acme.entities.student5.maintenanceRecord.MaintenanceRecord;
import acme.entities.student5.technician.Technician;

@GuiService
public class TechnicianMaintenanceRecordShowService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		int recordId = super.getRequest().getData("id", int.class);
		MaintenanceRecord record = this.repository.findMaintenanceRecordById(recordId);
		Technician technician = record == null ? null : record.getTechnician();

		boolean status = record != null && (record.getStatus() != Status.IN_PROGRESS || super.getRequest().getPrincipal().hasRealm(technician));
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		MaintenanceRecord record = this.repository.findMaintenanceRecordById(id);
		super.getBuffer().addData(record);
	}

	@Override
	public void unbind(final MaintenanceRecord record) {
		Dataset dataset;
		SelectChoices statusChoices;
		SelectChoices aircraftChoices;
		Collection<Aircraft> aircrafts;

		statusChoices = SelectChoices.from(Status.class, record.getStatus());

		aircrafts = this.repository.findAvailableAircrafts();
		aircraftChoices = SelectChoices.from(aircrafts, "model", record.getAircraft());

		dataset = super.unbindObject(record, "technician.identity.name", "maintenanceDate", "nextInspectionDueDate", "status", "estimatedCost", "notes");
		dataset.put("status", statusChoices.getSelected().getKey());
		dataset.put("statuses", statusChoices);
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);

		// Collection<Task> tasks = this.repository.findTasksByMaintenanceRecordId(record.getId());

		super.getResponse().addData(dataset);
	}
}
