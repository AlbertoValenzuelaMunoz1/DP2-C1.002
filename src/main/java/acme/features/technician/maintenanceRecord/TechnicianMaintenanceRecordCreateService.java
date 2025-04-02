
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
public class TechnicianMaintenanceRecordCreateService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		Technician technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		boolean status = technician != null;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecord record = new MaintenanceRecord();
		record.setStatus(Status.PENDING);
		super.getBuffer().addData(record);
	}

	@Override
	public void bind(final MaintenanceRecord record) {
		// Vincula los campos automáticamente configurados
		super.bind(record, "maintenanceDate", "nextInspectionDueDate", "status", "estimatedCost", "notes", "aircraft");

		// Asigna manualmente la aeronave al record, buscando por su ID
		int aircraftId = super.getRequest().getData("aircraft", int.class);
		Aircraft aircraft = this.repository.findAircraftById(aircraftId);
		record.setAircraft(aircraft);
	}

	@Override
	public void validate(final MaintenanceRecord record) {
		if (record.getAircraft() == null)
			super.state(false, "aircraft", "technician.maintenance-record.error.no-aircraft");
	}

	@Override
	public void perform(final MaintenanceRecord record) {
		Technician technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		record.setTechnician(technician);
		this.repository.save(record);
	}

	@Override
	public void unbind(final MaintenanceRecord record) {
		Collection<Aircraft> aircrafts = this.repository.findAvailableAircrafts();
		SelectChoices aircraftChoices = SelectChoices.from(aircrafts, "model", record.getAircraft());
		SelectChoices statusChoices = SelectChoices.from(Status.class, record.getStatus());

		Dataset dataset = super.unbindObject(record, "maintenanceDate", "nextInspectionDueDate", "status", "estimatedCost", "notes");
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("statuses", statusChoices);

		super.getResponse().addData(dataset);
	}

}
