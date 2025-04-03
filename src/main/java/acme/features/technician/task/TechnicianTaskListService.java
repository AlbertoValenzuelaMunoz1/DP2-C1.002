
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.student5.Task.Task;
import acme.entities.student5.technician.Technician;

@GuiService
public class TechnicianTaskListService extends AbstractGuiService<Technician, Task> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianTaskRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Technician technician;

		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		status = super.getRequest().getPrincipal().hasRealm(technician);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Task> task;
		int technicianId;

		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
		task = this.repository.findTasksByTechnicianId(technicianId);

		super.getBuffer().addData(task);
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration");
		super.getResponse().addData(dataset);
	}
}
