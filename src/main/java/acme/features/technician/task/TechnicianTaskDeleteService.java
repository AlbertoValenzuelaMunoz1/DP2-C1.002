
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.TaskType;
import acme.entities.student5.Task.Task;
import acme.entities.student5.Task.TaskRecord;
import acme.entities.student5.technician.Technician;

@GuiService
public class TechnicianTaskDeleteService extends AbstractGuiService<Technician, Task> {

	// Internal state ------------------------------------------------------------

	@Autowired
	private TechnicianTaskRepository repository;

	// AbstractGuiService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Task task;
		Technician technician;

		masterId = super.getRequest().getData("id", int.class);
		task = this.repository.findTaskById(masterId);
		technician = task == null ? null : task.getTechnician();
		status = task != null && super.getRequest().getPrincipal().hasRealm(technician); // eliminado draftMode

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Task task = this.repository.findTaskById(id);

		super.getBuffer().addData(task);
	}

	@Override
	public void bind(final Task task) {
		super.bindObject(task, "taskType", "description", "priority", "estimatedDuration");
	}

	@Override
	public void perform(final Task task) {
		Collection<TaskRecord> taskRecordsInvolved = this.repository.findTaskRecordByTaskId(task.getId());
		this.repository.deleteAll(taskRecordsInvolved);
		this.repository.delete(task);
	}

	@Override
	public void unbind(final Task task) {
		SelectChoices types = SelectChoices.from(TaskType.class, task.getTaskType());

		Dataset dataset = super.unbindObject(task, "technician.identity.name", "taskType", "description", "priority", "estimatedDuration");
		dataset.put("types", types);

		super.getResponse().addData(dataset);
	}
}
