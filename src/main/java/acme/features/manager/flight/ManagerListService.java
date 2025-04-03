
package acme.features.manager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.student1.flight.Flight;
import acme.realms.Manager;

@GuiService
public class ManagerListService extends AbstractGuiService<Manager, Flight> {

	@Autowired
	private ManagerRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Flight> flights;
		int managerId;

		managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		flights = this.repository.findFlightsByManagerId(managerId);

		super.getBuffer().addData(flights);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;
		System.out.println("Al clicar");

		dataset = super.unbindObject(flight, "tag", "transfer", "cost", "draftMode");
		super.addPayload(dataset, flight, "description");

		super.getResponse().addData(dataset);
	}

}
