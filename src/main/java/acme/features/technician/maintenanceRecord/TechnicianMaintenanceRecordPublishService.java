
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.Status;
import acme.entities.group.aircraft.Aircraft;
import acme.entities.student5.Task.Task;
import acme.entities.student5.maintenanceRecord.MaintenanceRecord;
import acme.entities.student5.technician.Technician;

@GuiService
public class TechnicianMaintenanceRecordPublishService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int recordId;
		MaintenanceRecord record;
		Technician technician;

		recordId = super.getRequest().getData("id", int.class);
		record = this.repository.findMaintenanceRecordById(recordId);
		technician = record != null ? record.getTechnician() : null;

		status = record != null && (record.getStatus() == Status.IN_PROGRESS || record.getStatus() == Status.COMPLETED) && super.getRequest().getPrincipal().hasRealm(technician);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecord record;
		int id;

		id = super.getRequest().getData("id", int.class);
		record = this.repository.findMaintenanceRecordById(id);

		super.getBuffer().addData(record);
	}

	@Override
	public void bind(final MaintenanceRecord record) {
		super.bindObject(record, "maintanenceMoment", "status", "nextMaintanence", "estimatedCost", "notes");
	}

	@Override
	public void validate(final MaintenanceRecord record) {

		Collection<Task> tasks = this.repository.findTasksByMaintenanceRecordId(record.getId());
		boolean atLeastOnePublished = tasks.stream().anyMatch(task -> task.getTaskRecord().getMaintenanceRecord().getStatus() == Status.IN_PROGRESS || task.getTaskRecord().getMaintenanceRecord().getStatus() == Status.COMPLETED);

		super.state(atLeastOnePublished, "*", "technician.maintanence-record.error.noPublishedTasks.message");

		super.state(record.getStatus() != Status.PENDING, "*", "technician.maintanence-record.error.invalid-status-pending");
	}

	@Override
	public void perform(final MaintenanceRecord record) {

		record.setStatus(Status.COMPLETED);
		this.repository.save(record);
	}

	@Override
	public void unbind(final MaintenanceRecord record) {
		Dataset dataset;
		SelectChoices choices;
		SelectChoices aircraftChoices;
		Collection<Aircraft> aircrafts;

		aircrafts = this.repository.findAvailableAircrafts();
		aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", record.getAircraft());
		choices = SelectChoices.from(Status.class, record.getStatus());

		dataset = super.unbindObject(record, "maintanenceMoment", "status", "nextMaintanence", "estimatedCost", "notes");
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("statuses", choices);

		super.getResponse().addData(dataset);
	}
}
