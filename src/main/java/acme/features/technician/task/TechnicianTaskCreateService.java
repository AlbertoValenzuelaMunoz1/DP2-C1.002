
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
public class TechnicianTaskCreateService extends AbstractGuiService<Technician, Task> {

	// Internal state ------------------------------------------------------------

	@Autowired
	private TechnicianTaskRepository repository;

	// AbstractGuiService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Task task = new Task();
		Technician technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		task.setTechnician(technician);

		super.getBuffer().addData(task);
	}

	@Override
	public void bind(final Task task) {
		super.bindObject(task, "taskType", "description", "priority", "estimatedDuration");
	}

	@Override
	public void validate(final Task task) {
		// Puedes añadir validaciones si es necesario
	}

	@Override
	public void perform(final Task task) {
		this.repository.save(task);
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;
		SelectChoices types;

		types = SelectChoices.from(TaskType.class, task.getTaskType());

		dataset = super.unbindObject(task, "technician.identity.name", "taskType", "description", "priority", "estimatedDuration");
		dataset.put("types", types);

		super.getResponse().addData(dataset);
	}
}
