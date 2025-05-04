
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
public class LegFlightCreateService extends AbstractGuiService<Manager, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private LegFlightRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Flight flight;
		Manager manager;

		System.out.println("authorise");

		masterId = super.getRequest().getData("masterId", int.class);
		Optional<Flight> optionalFlight = this.repository.findFlightById(masterId);
		flight = optionalFlight.isPresent() ? optionalFlight.get() : null;
		manager = flight == null ? null : flight.getManager();
		status = flight != null && flight.isDraftMode() && super.getRequest().getPrincipal().hasRealm(manager);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int masterId;
		Flight flight;

		System.out.println("load");

		masterId = super.getRequest().getData("masterId", int.class);
		Optional<Flight> optionalFlight = this.repository.findFlightById(masterId);
		flight = optionalFlight.isPresent() ? optionalFlight.get() : null;

		leg = new Leg();
		leg.setFlight(flight);
		leg.setDraftMode(true);

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {

		System.out.println("bind");
		super.bindObject(leg, "flightNumberDigits", "scheduledDeparture", "scheduledArrival", "departureAirport", "arrivalAirport", "aircraft", "flight", "status");
	}

	@Override
	public void validate(final Leg leg) {
		boolean statusAircraft;
		boolean destAndArrvAirport;
		boolean destAndArrvScheduled;
		boolean transferFlight;
		boolean scheduledDepartureOrder;
		Flight flight;

		transferFlight = false;

		flight = leg.getFlight();

		Collection<Leg> legs = this.repository.findLegsOrderedByFlightId(flight.getId());

		Optional<Leg> optionalLastLeg = legs != null ? legs.stream().reduce((first, second) -> second) : null;

		Leg lastLeg = optionalLastLeg == null ? null : optionalLastLeg.get();

		if (lastLeg != null && !flight.isTransfer())
			transferFlight = leg.getDepartureAirport().equals(lastLeg.getArrivalAirport());

		scheduledDepartureOrder = true;

		if (lastLeg != null)
			scheduledDepartureOrder = leg.getScheduledDeparture().after(lastLeg.getScheduledArrival());

		destAndArrvScheduled = leg.getScheduledArrival().after(leg.getScheduledDeparture());

		destAndArrvAirport = leg.getArrivalAirport().equals(leg.getDepartureAirport());

		statusAircraft = leg.getAircraft().getStatus().equals(AircraftStatus.ACTIVE);

		super.state(statusAircraft, "aircraft", "acme.validation.manager.leg.statusAircraft.message");
		super.state(!destAndArrvAirport, "*", "el aeropuerto de salida no puede ser el mismo q de llegada");
		super.state(destAndArrvScheduled, "scheduledArrival", "acme.validation.manager.leg.departureTime.message");
		super.state(transferFlight, "departureAirport", "El aeropuerto de salida tiene que ser el aeropuerto de llegada del tramo anterior: " + lastLeg.getArrivalAirport().getIataCode());
		super.state(scheduledDepartureOrder, "scheduledDeparture", "acme.validation.manager.leg.scheduledDepartureOrder.message" + ": " + lastLeg.getScheduledArrival());
	}

	@Override
	public void perform(final Leg leg) {
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
		int managerId;
		int flightId;

		flightId = leg.getFlight().getId();

		choicesStatus = SelectChoices.from(LegStatus.class, leg.getStatus());

		managerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		airports = this.repository.findAllAirports();

		Collection<Leg> legsFlight = this.repository.findLegsByFlightId(flightId);

		Set<Airport> arrivedAirports = new HashSet<Airport>();
		Set<Airport> departuredAirports = new HashSet<Airport>();

		if (legsFlight != null)
			for (Leg l : legsFlight) {
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

		super.getResponse().addData(dataset);
	}

}
