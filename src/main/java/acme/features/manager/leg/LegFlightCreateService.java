
package acme.features.manager.leg;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
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
		boolean validId = true;
		int masterId;
		Flight flight;
		Manager manager;

		if (super.getRequest().getMethod().equals("POST")) {
			int id = super.getRequest().getData("id", int.class, 0);
			validId = id == 0;
		}

		masterId = super.getRequest().getData("masterId", int.class);
		Optional<Flight> optionalFlight = this.repository.findFlightById(masterId);
		flight = optionalFlight.isPresent() ? optionalFlight.get() : null;
		manager = flight == null ? null : flight.getManager();
		status = flight != null && flight.isDraftMode() && super.getRequest().getPrincipal().hasRealm(manager) && validId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int masterId;
		Flight flight;

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

		super.bindObject(leg, "flightNumberDigits", "scheduledDeparture", "scheduledArrival", "departureAirport", "arrivalAirport", "aircraft", "status");
	}

	@Override
	public void validate(final Leg leg) {
		boolean statusAircraft;
		boolean statusSchedule;
		boolean statusDestAndArrvAirport;
		boolean statusShareAircraft;
		boolean statusAircraftAirline;
		boolean statusFlightNumber;
		boolean statusFuture;
		Collection<Leg> allLegs;
		Collection<Leg> flightsLegs;

		//flightNumberDigits
		Collection<String> flightNumbers = this.repository.findFlightNumbers();

		statusFlightNumber = flightNumbers.stream().allMatch(fn -> !leg.getFlightNumberDigits().equals(fn));

		//aeropuertos de llegada y salida distintos
		statusDestAndArrvAirport = leg.getArrivalAirport() != null && leg.getDepartureAirport() != null ? !leg.getArrivalAirport().equals(leg.getDepartureAirport()) : null;

		//aircrafts no solapen
		allLegs = this.repository.findAllLegs();

		List<Leg> overlapLegs = allLegs != null && leg.getScheduledArrival() != null && leg.getScheduledDeparture() != null ? allLegs.stream()
			.filter(l -> !(leg.getScheduledDeparture().after(l.getScheduledArrival()) && leg.getScheduledArrival().after(l.getScheduledArrival()) || leg.getScheduledDeparture().before(l.getScheduledDeparture())
				&& leg.getScheduledArrival().before(l.getScheduledDeparture()) && !(leg.getScheduledArrival().after(l.getScheduledArrival()) && leg.getScheduledArrival().before(l.getScheduledDeparture()))
				&& !(leg.getScheduledDeparture().after(l.getScheduledArrival()) && leg.getScheduledArrival().before(l.getScheduledDeparture()))))
			.toList() : null;

		statusShareAircraft = overlapLegs == null || leg.getAircraft() == null || overlapLegs.stream().allMatch(l -> leg.getAircraft() != l.getAircraft());

		//legs no solapen
		flightsLegs = this.repository.findLegsByFlightId(leg.getFlight().getId());

		statusSchedule = flightsLegs == null || leg.getScheduledArrival() == null || leg.getScheduledDeparture() == null
			|| flightsLegs.stream()
				.allMatch(l -> leg.getScheduledDeparture().after(l.getScheduledArrival()) && leg.getScheduledArrival().after(l.getScheduledArrival()) || leg.getScheduledDeparture().before(l.getScheduledDeparture())
					&& leg.getScheduledArrival().before(l.getScheduledDeparture()) && !(leg.getScheduledArrival().after(l.getScheduledArrival()) && leg.getScheduledArrival().before(l.getScheduledDeparture()))
					&& !(leg.getScheduledDeparture().after(l.getScheduledArrival()) && leg.getScheduledArrival().before(l.getScheduledDeparture())));

		//aircraft esté activo
		statusAircraft = leg.getAircraft() == null || leg.getAircraft().getStatus().equals(AircraftStatus.ACTIVE);

		//aircraft pertenezca misma airline que manager
		statusAircraftAirline = leg.getAircraft() == null || leg.getAircraft().getAirline().equals(leg.getFlight().getManager().getAirline());

		//legs en el futuro
		statusFuture = leg.getScheduledArrival() == null || leg.getScheduledDeparture() == null || leg.getScheduledArrival().after(MomentHelper.getCurrentMoment()) && leg.getScheduledDeparture().after(MomentHelper.getCurrentMoment());

		super.state(statusFuture, "*", "acme.validation.manager.leg.statusFuture.message");
		super.state(statusDestAndArrvAirport, "*", "acme.validation.manager.leg.statusDestAndArrvAirport.message");
		super.state(statusSchedule, "*", "acme.validation.manager.leg.statusSchedule.message");
		super.state(statusAircraft, "aircraft", "acme.validation.manager.leg.statusAircraft.message");
		super.state(statusShareAircraft, "aircraft", "acme.validation.manager.leg.statusShareAircraft.message");
		super.state(statusAircraftAirline, "aircraft", "acme.validation.manager.leg.statusAircraftAirline.message");
		super.state(statusFlightNumber, "flightNumberDigits", "acme.validation.manager.leg.statusFlightNumber.message");
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

		choicesStatus = SelectChoices.from(LegStatus.class, leg.getStatus());

		airports = this.repository.findAllAirports();
		aircrafts = this.repository.findAllAircrafts();
		choicesArrivalAirports = SelectChoices.from(airports, "iataCode", leg.getArrivalAirport());
		choicesDepartureAirports = SelectChoices.from(airports, "iataCode", leg.getDepartureAirport());
		choicesAircraft = SelectChoices.from(aircrafts, "model", leg.getAircraft());

		dataset = super.unbindObject(leg, "flightNumberDigits", "scheduledDeparture", "scheduledArrival", "departureAirport", "arrivalAirport", "aircraft", "flight.tag", "status", "draftMode");
		dataset.put("masterId", leg.getFlight().getId());
		dataset.put("arrivalAirports", choicesArrivalAirports);
		dataset.put("departureAirports", choicesDepartureAirports);
		dataset.put("aircrafts", choicesAircraft);
		dataset.put("statuses", choicesStatus);
		dataset.put("durationInHours", leg.durationInHours());

		super.getResponse().addData(dataset);
	}

}
