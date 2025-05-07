
package acme.features.manager.leg;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.AircraftStatus;
import acme.entities.group.aircraft.Aircraft;
import acme.entities.group.airport.Airport;
import acme.entities.student1.flight.Flight;
import acme.entities.student1.leg.Leg;
import acme.entities.student1.leg.LegStatus;
import acme.realms.Manager;

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
		boolean mode;
		boolean statusAircraft;
		boolean destAndArrvAirport;
		boolean destAndArrvScheduled;
		boolean transferFlight;
		boolean scheduledDepartureOrder;
		Flight flight;

		mode = leg.isDraftMode();

		transferFlight = false;

		flight = leg.getFlight();

		Collection<Leg> legs = this.repository.findLegsOrderedByFlightId(flight.getId());

		Optional<Leg> optionalLastLeg = legs != null ? legs.stream().reduce((first, second) -> second) : null;

		Leg lastLeg = optionalLastLeg != null && optionalLastLeg.isPresent() ? optionalLastLeg.get() : null;

		if (lastLeg != null)
			transferFlight = leg.getDepartureAirport() == null || lastLeg == null || leg.getDepartureAirport().equals(lastLeg.getArrivalAirport());

		scheduledDepartureOrder = true;

		if (lastLeg != null)
			scheduledDepartureOrder = leg.getScheduledDeparture() == null || lastLeg == null || leg.getScheduledDeparture().after(lastLeg.getScheduledArrival());

		destAndArrvScheduled = leg.getScheduledArrival() == null || leg.getScheduledDeparture() == null || leg.getScheduledArrival().after(leg.getScheduledDeparture());

		destAndArrvAirport = leg.getArrivalAirport() == null || leg.getDepartureAirport() == null || leg.getArrivalAirport().equals(leg.getDepartureAirport());

		statusAircraft = leg.getAircraft() == null || leg.getAircraft().getStatus().equals(AircraftStatus.ACTIVE);

		super.state(mode, "draftMode", "tiene q estar en modo borrador");
	}

	@Override
	public void perform(final Leg leg) {
		System.out.println("perform");
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;
		Collection<Airport> airports;
		Collection<Aircraft> aircrafts;
		SelectChoices choicesArrivalAirports;
		SelectChoices choicesDepartureAirports;
		SelectChoices choicesAircraft;
		SelectChoices choicesStatus;
		int flightId;

		flightId = leg.getFlight().getId();

		choicesStatus = SelectChoices.from(LegStatus.class, leg.getStatus());

		airports = this.repository.findAllAirports();

		Collection<Leg> legsFlight = this.repository.findLegsByFlightId(flightId);

		Set<Airport> arrivedAirports = new HashSet<Airport>();
		Set<Airport> departuredAirports = new HashSet<Airport>();

		if (legsFlight != null)
			for (Leg l : legsFlight.stream().dropWhile(l -> leg.getScheduledDeparture().after(l.getScheduledDeparture())).toList()) {
				arrivedAirports.add(l.getArrivalAirport());
				arrivedAirports.add(l.getDepartureAirport());

				departuredAirports.add(l.getDepartureAirport());
			}

		Set<Airport> avaibleArrivalAirports = airports.stream().filter(air -> !arrivedAirports.contains(air)).collect(Collectors.toSet());
		Set<Airport> avaibleDepartureAirports = airports.stream().filter(air -> !departuredAirports.contains(air)).collect(Collectors.toSet());

		aircrafts = this.repository.findAllAircrafts();
		choicesArrivalAirports = SelectChoices.from(avaibleArrivalAirports, "iataCode", leg.getArrivalAirport());
		choicesDepartureAirports = SelectChoices.from(avaibleDepartureAirports, "iataCode", leg.getDepartureAirport());
		choicesAircraft = SelectChoices.from(aircrafts, "model", leg.getAircraft());

		dataset = super.unbindObject(leg, "flightNumberDigits", "scheduledDeparture", "scheduledArrival", "departureAirport", "arrivalAirport", "aircraft", "flight", "status", "draftMode");
		dataset.put("masterId", leg.getFlight().getId());
		dataset.put("arrivalAirports", choicesArrivalAirports);
		dataset.put("departureAirports", choicesDepartureAirports);
		dataset.put("aircrafts", choicesAircraft);
		dataset.put("statuses", choicesStatus);
		dataset.put("durationInHours", leg.durationInHours());

		super.getResponse().addData(dataset);
	}

}
