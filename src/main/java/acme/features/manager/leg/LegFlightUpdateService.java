
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

		masterId = super.getRequest().getData("id", int.class);

		Optional<Leg> optional = this.repository.findLegById(masterId);

		leg = optional.isPresent() ? optional.get() : null;

		manager = leg == null ? null : leg.getFlight().getManager();

		status = leg != null && leg.isDraftMode() && super.getRequest().getPrincipal().hasRealm(manager);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		Optional<Leg> optionalLeg = this.repository.findLegById(id);
		leg = optionalLeg.isPresent() ? optionalLeg.get() : null;

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		super.bindObject(leg, "scheduledDeparture", "scheduledArrival", "stauts", "departureAirport", "arrivalAirport", "aircraft");
	}

	@Override
	public void validate(final Leg leg) {
		boolean mode;
		boolean statusAircraft;
		boolean statusSchedule;
		boolean statusDestAndArrvAirport;
		boolean statusShareAircraft;
		boolean statusAircraftAirline;
		boolean statusFuture;
		boolean statusArrvAfterDepart;
		Collection<Leg> allLegs;
		Collection<Leg> flightsLegs;

		mode = leg.isDraftMode();

		//aeropuertos de llegada y salida distintos
		statusDestAndArrvAirport = leg.getArrivalAirport() == null || leg.getDepartureAirport() == null || !leg.getArrivalAirport().equals(leg.getDepartureAirport());

		//aircrafts no solapen
		allLegs = this.repository.findAllLegs();

		allLegs = allLegs != null ? allLegs.stream().filter(l -> !l.equals(leg)).toList() : null;

		List<Leg> overlapLegs = allLegs != null && leg.getScheduledArrival() != null
			&& leg.getScheduledDeparture() != null
				? allLegs.stream()
					.filter(l -> leg.getScheduledDeparture().after(l.getScheduledArrival()) && leg.getScheduledDeparture().before(l.getScheduledDeparture())
						|| leg.getScheduledArrival().after(l.getScheduledArrival()) && leg.getScheduledArrival().before(l.getScheduledDeparture()))
					.toList()
				: null;

		statusShareAircraft = overlapLegs == null || leg.getAircraft() == null || overlapLegs.stream().allMatch(l -> leg.getAircraft() != l.getAircraft());

		//legs no solapen
		flightsLegs = this.repository.findLegsByFlightId(leg.getFlight().getId());

		flightsLegs = flightsLegs != null ? flightsLegs.stream().filter(l -> !l.equals(leg)).toList() : null;

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

		//llegada despues de salida
		statusArrvAfterDepart = leg.getScheduledArrival() != null && leg.getScheduledDeparture() != null ? leg.getScheduledArrival().after(leg.getScheduledDeparture()) : null;

		super.state(statusArrvAfterDepart, "*", "acme.validation.manager.leg.statusArrvAfterDepart.message");
		super.state(statusFuture, "*", "acme.validation.manager.leg.statusFuture.message");
		super.state(statusDestAndArrvAirport, "*", "acme.validation.manager.leg.statusDestAndArrvAirport.message");
		super.state(statusSchedule, "*", "acme.validation.manager.leg.statusSchedule.message");
		super.state(statusAircraft, "aircraft", "acme.validation.manager.leg.statusAircraft.message");
		super.state(statusShareAircraft, "aircraft", "acme.validation.manager.leg.statusShareAircraft.message");
		super.state(statusAircraftAirline, "aircraft", "acme.validation.manager.leg.statusAircraftAirline.message");
		super.state(mode, "draftMode", "acme.validation.manager.leg.statusDraftMode.message");

	}

	@Override
	public void perform(final Leg leg) {
		this.repository.save(leg);
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

		dataset = super.unbindObject(leg, "flightNumberDigits", "scheduledDeparture", "scheduledArrival", "departureAirport", "arrivalAirport", "aircraft", "flight.tag", "status", "draftMode");
		dataset.put("masterId", leg.getFlight().getId());
		dataset.put("flights", choicesFlight);
		dataset.put("arrivalAirports", choicesArrivalAirports);
		dataset.put("departureAirports", choicesDepartureAirports);
		dataset.put("aircrafts", choicesAircraft);
		dataset.put("statuses", choicesStatus);
		dataset.put("durationInHours", leg.durationInHours());

		super.getResponse().addData(dataset);
	}

}
