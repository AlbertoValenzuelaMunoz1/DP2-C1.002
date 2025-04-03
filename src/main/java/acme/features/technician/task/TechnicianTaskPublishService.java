
package acme.features.technician.task;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.TaskType;
import acme.entities.student5.Task.Task;
import acme.entities.student5.technician.Technician;

@GuiService
public class TechnicianTaskPublishService extends AbstractGuiService<Technician, Task> {

	@Autowired
	private TechnicianTaskRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int taskId;
		Task task;
		Technician tech;

		taskId = super.getRequest().getData("id", int.class);
		task = this.repository.findTaskById(taskId);
		tech = task == null ? null : task.getTechnician();

		status = task != null && super.getRequest().getPrincipal().hasRealm(tech);

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
		this.repository.save(task);
	}

	@Override
	public void unbind(final Task task) {
		SelectChoices choices = SelectChoices.from(TaskType.class, task.getTaskType());

		Dataset dataset = super.unbindObject(task, "technician.identity.name", "taskType", "description", "priority", "estimatedDuration");
		dataset.put("taskType", choices);

		super.getResponse().addData(dataset);
	}
}
