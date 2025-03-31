
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
public class LegFlightUpdateService extends AbstractGuiService<Manager, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private LegFlightRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Leg leg;
		Manager manager;

		System.out.println("afbaosibfisab");

		masterId = super.getRequest().getData("id", int.class);
		System.out.println("master");
		Optional<Leg> optional = this.repository.findLegById(masterId);
		System.out.println("2");
		System.out.println(optional.isEmpty());
		System.out.println("3");
		leg = optional.isPresent() ? optional.get() : null;
		System.out.println("4");
		manager = leg == null ? null : leg.getFlight().getManager();
		System.out.println("5");
		status = leg != null && leg.isDraftMode() && super.getRequest().getPrincipal().hasRealm(manager);
		System.out.println("6");

		super.getResponse().setAuthorised(status);
		System.out.println("7");
	}

	@Override
	public void load() {
		Leg leg;
		int id;

		System.out.println("aqui");

		id = super.getRequest().getData("id", int.class);
		Optional<Leg> optionalLeg = this.repository.findLegById(id);
		leg = optionalLeg.isPresent() ? optionalLeg.get() : null;

		System.out.println(leg);

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		System.out.println("bind");
		super.bindObject(leg, "flightNumberDigits", "scheduledDeparture", "scheduledArrival", "departureAirport", "arrivalAirport", "aircraft", "flight", "status");
	}

	@Override
	public void validate(final Leg leg) {
		System.out.println("Validate");
		boolean mode;

		mode = leg.isDraftMode();

		super.state(mode, "drafMode", "tiene q estar en modo borrador");
	}

	@Override
	public void perform(final Leg leg) {
		System.out.println("perform");
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		System.out.println("unbind");
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

		dataset = super.unbindObject(leg, "flightNumberDigits", "scheduledDeparture", "scheduledArrival", "departureAirport", "arrivalAirport", "aircraft", "flight", "status", "drafMode");
		dataset.put("masterId", leg.getFlight().getId());
		dataset.put("flights", choicesFlight);
		dataset.put("arrivalAirports", choicesArrivalAirports);
		dataset.put("departureAirports", choicesDepartureAirports);
		dataset.put("aircrafts", choicesAircraft);
		dataset.put("statuses", choicesStatus);

		super.getResponse().addData(dataset);
	}

}
