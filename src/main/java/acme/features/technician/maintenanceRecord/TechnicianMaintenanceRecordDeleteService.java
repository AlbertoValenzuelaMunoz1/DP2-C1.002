
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.Status;
import acme.entities.group.aircraft.Aircraft;
import acme.entities.student5.Task.TaskRecord;
import acme.entities.student5.maintenanceRecord.MaintenanceRecord;
import acme.entities.student5.technician.Technician;
import acme.features.technician.involvedIn.TechnicianInvolvedInRepository;

@GuiService
public class TechnicianMaintenanceRecordDeleteService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository	repository;

	@Autowired
	private TechnicianInvolvedInRepository			involvedRepository;


	@Override
	public void authorise() {
		boolean status;
		int recordId;
		MaintenanceRecord record;
		Technician tech;

		recordId = super.getRequest().getData("id", int.class);
		record = this.repository.findMaintenanceRecordById(recordId);
		tech = record != null ? record.getTechnician() : null;

		status = record != null && (record.getStatus() == Status.IN_PROGRESS || record.getStatus() == Status.COMPLETED) && super.getRequest().getPrincipal().hasRealm(tech);

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

		if (record.getStatus() != Status.IN_PROGRESS && record.getStatus() != Status.COMPLETED)
			super.state(false, "status", "technician.maintenance-record.error.invalid-status");
	}

	@Override
	public void perform(final MaintenanceRecord record) {

		Collection<TaskRecord> involvedEntities = this.repository.findAllInvolvedInById(record.getId());
		this.involvedRepository.deleteAll(involvedEntities);

		this.repository.delete(record);
	}

	@Override
	public void unbind(final MaintenanceRecord record) {
		Dataset dataset;
		SelectChoices choices;
		SelectChoices aircraftChoices;

		Collection<Aircraft> aircrafts = this.repository.findAvailableAircrafts();
		aircraftChoices = SelectChoices.from(aircrafts, "model", record.getAircraft());
		choices = SelectChoices.from(Status.class, record.getStatus());

		dataset = super.unbindObject(record, "maintanenceMoment", "status", "nextMaintanence", "estimatedCost", "notes");
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("statuses", choices);

		super.getResponse().addData(dataset);
	}
}
