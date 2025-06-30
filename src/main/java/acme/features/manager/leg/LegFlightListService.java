
package acme.features.manager.leg;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.student1.flight.Flight;
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
		Optional<Flight> optionalFlight;

		flightId = super.getRequest().getData("masterId", int.class);

		optionalFlight = this.repository.findFlightById(flightId);

		manager = optionalFlight.isEmpty() ? null : optionalFlight.get().getManager();

		status = super.getRequest().getPrincipal().hasRealm(manager);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Leg> legs;
		int masterId;
		boolean showCreate;
		Optional<Flight> optionalFlight;

		masterId = super.getRequest().getData("masterId", int.class);
		legs = this.repository.findLegsByFlightId(masterId);

		super.getBuffer().addData(legs);

		super.getResponse().addGlobal("masterId", masterId);

		optionalFlight = this.repository.findFlightById(masterId);

		Flight flight = optionalFlight.isPresent() ? optionalFlight.get() : null;

		showCreate = flight != null ? flight.isDraftMode() : null;

		super.getResponse().addGlobal("showCreate", showCreate);

	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;

		dataset = super.unbindObject(leg, "flightNumberDigits", "scheduledDeparture", "scheduledArrival", "departureAirport", "arrivalAirport", "status");
		super.addPayload(dataset, leg, "aircraft", "flight");

		super.getResponse().addData(dataset);
	}

}
