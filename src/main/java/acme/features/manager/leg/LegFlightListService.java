
package acme.features.manager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.student1.leg.Leg;
import acme.realms.Manager;

@GuiService
public class LegFlightListService extends AbstractGuiService<Manager, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private LegFlightRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int flightId;
		Manager manager;

		System.out.println("Aqui1");

		flightId = super.getRequest().getData("masterId", int.class);
		manager = this.repository.findFlightById(flightId).get().getManager();

		System.out.println("Aqui2");

		status = super.getRequest().getPrincipal().hasRealm(manager);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Leg> legs;
		int masterId;

		System.out.println("Aqui3");

		masterId = super.getRequest().getData("masterId", int.class);
		legs = this.repository.findLegsByFlightId(masterId);

		System.out.println("Aqui4");

		super.getBuffer().addData(legs);

		System.out.println("Aqui5");

		super.getResponse().addGlobal("masterId", masterId);

	}

	@Override
	public void unbind(final Leg leg) {
		int masterId;
		Dataset dataset;
		boolean showCreate;

		System.out.println("Aqui6");

		masterId = super.getRequest().getData("masterId", int.class);
		System.out.println("Aqui7");
		dataset = super.unbindObject(leg, "flightNumberDigits", "scheduledDeparture", "scheduledArrival", "departureAirport", "arrivalAirport", "status");
		super.addPayload(dataset, leg, "aircraft", "flight");

		showCreate = leg != null && leg.getFlight().isDraftMode();

		super.getResponse().addGlobal("showCreate", showCreate);
		super.getResponse().addData(dataset);
	}

}
