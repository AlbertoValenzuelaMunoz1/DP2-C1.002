
package acme.features.manager;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.student1.flight.Flight;
import acme.entities.student1.manager.Manager;

@GuiService
public class ManagerShowService extends AbstractGuiService<Manager, Flight> {

	@Autowired
	private ManagerRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int flightId;
		Manager manager;
		Flight flight;

		flightId = super.getRequest().getData("id", int.class);
		System.out.println("id" + flightId);
		flight = this.repository.findFlightById(flightId).orElse(null);
		System.out.println("fli" + flight);
		manager = flight == null ? null : flight.getManager();
		System.out.println(manager);
		System.out.println(super.getRequest().getPrincipal().getActiveRealm());

		status = flight != null && super.getRequest().getPrincipal().hasRealm(manager);

		System.out.println("esnull?   " + flight != null + "manager?    " + super.getRequest().getPrincipal().hasRealm(manager) + "drafmode?   " + !flight.isDraftMode());
		System.out.println("stratus   " + status);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Flight flight;
		int id;

		id = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(id).get();

		System.out.println(flight);

		super.getBuffer().addData(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		dataset = super.unbindObject(flight, "tag", "transfer", "cost", "draftMode", "description");
		dataset.put("origin", flight.originCity());
		dataset.put("destiny", flight.destinationCity());
		dataset.put("departureDate", flight.scheduledDeparture());
		dataset.put("arrivalDate", flight.scheduledArrival());
		dataset.put("numberOfLayovers", flight.numberOfLayovers());

		super.getResponse().addData(dataset);
	}

}
