
package acme.features.manager;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.student1.flight.Flight;
import acme.entities.student1.manager.Manager;

@GuiService
public class ManagerCreateService extends AbstractGuiService<Manager, Flight> {

	@Autowired
	private ManagerRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Manager manager;
		Flight object;

		manager = (Manager) super.getRequest().getPrincipal().getActiveRealm();

		object = new Flight();
		object.setManager(manager);
		object.setDraftMode(true);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Flight flight) {

		super.bindObject(flight, "tag", "transfer", "cost", "description");

	}

	@Override
	public void validate(final Flight flight) {
		;
	}

	@Override
	public void perform(final Flight flight) {
		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		int managerId;
		Dataset dataset;

		managerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		dataset = super.unbindObject(flight, "tag", "transfer", "cost", "draftMode", "description");

		super.getResponse().addData(dataset);
	}

}
