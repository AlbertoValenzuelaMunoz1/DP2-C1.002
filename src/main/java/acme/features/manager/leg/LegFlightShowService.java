
package acme.features.manager.leg;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.group.aircraft.Aircraft;
import acme.entities.group.airport.Airport;
import acme.entities.student1.flight.Flight;
import acme.entities.student1.leg.Leg;
import acme.entities.student1.leg.LegStatus;
import acme.entities.student1.manager.Manager;

@GuiService
public class LegFlightShowService extends AbstractGuiService<Manager, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private LegFlightRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int legId;
		Leg leg;
		Manager manager;

		System.out.println("fodjsbgodsbgds");

		legId = super.getRequest().getData("id", int.class);
		System.out.println(legId);
		Optional<Leg> optionalLeg = this.repository.findLegById(legId);
		System.out.println(optionalLeg.isEmpty());
		if (optionalLeg.isPresent())
			System.out.println(optionalLeg.get());
		leg = optionalLeg.isPresent() ? optionalLeg.get() : null;

		System.out.println(leg);

		manager = leg.getFlight().getManager();

		System.out.println(manager);

		System.out.println(leg != null);
		System.out.println(super.getRequest().getPrincipal().hasRealm(manager));

		status = leg != null && super.getRequest().getPrincipal().hasRealm(manager);

		System.out.println(status);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		Optional<Leg> optionalLeg = this.repository.findLegById(id);
		leg = optionalLeg.isPresent() ? optionalLeg.get() : null;

		System.out.println(leg);

		super.getBuffer().addData(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;
		Collection<Flight> flights;
		Collection<Airport> airports;
		Collection<Aircraft> aircrafts;
		SelectChoices choicesFlight;
		SelectChoices choicesArrivalAirports;
		SelectChoices choicesDepartureAirports;
		SelectChoices choicesAircraft;
		SelectChoices choicesStatus;
		int managerId;

		choicesStatus = SelectChoices.from(LegStatus.class, leg.getStatus());

		managerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		flights = this.repository.findFlightsByManagerId(managerId);
		airports = this.repository.findAllAirports();
		aircrafts = this.repository.findAllAircrafts();
		choicesFlight = SelectChoices.from(flights, "tag", leg.getFlight());
		choicesArrivalAirports = SelectChoices.from(airports, "iataCode", leg.getArrivalAirport());
		choicesDepartureAirports = SelectChoices.from(airports, "iataCode", leg.getDepartureAirport());
		choicesAircraft = SelectChoices.from(aircrafts, "model", leg.getAircraft());

		dataset = super.unbindObject(leg, "flightNumberDigits", "scheduledDeparture", "scheduledArrival", "departureAirport", "arrivalAirport", "aircraft", "flight", "status", "draftMode");
		dataset.put("masterId", leg.getFlight().getId());
		dataset.put("flights", choicesFlight);
		dataset.put("arrivalAirports", choicesArrivalAirports);
		dataset.put("departureAirports", choicesDepartureAirports);
		dataset.put("aircrafts", choicesAircraft);
		dataset.put("statuses", choicesStatus);

		super.getResponse().addData(dataset);
	}

}
