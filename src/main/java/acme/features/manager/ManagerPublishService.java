
package acme.features.manager;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.student1.flight.Flight;
import acme.entities.student1.leg.Leg;
import acme.entities.student1.manager.Manager;

@GuiService
public class ManagerPublishService extends AbstractGuiService<Manager, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int flightId;
		Flight flight;
		Manager manager;

		flightId = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(flightId).orElse(null);
		manager = flight == null ? null : flight.getManager();
		status = flight != null && flight.isDraftMode() && super.getRequest().getPrincipal().hasRealm(manager);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Flight flight;
		int id;

		id = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(id).get();

		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {

		super.bindObject(flight, "tag", "transfer", "cost", "description");
	}

	@Override
	public void validate(final Flight flight) {
		Collection<Leg> legs = this.repository.findLegsByFlightId(flight.getId());
		boolean hasLeg = !legs.isEmpty();

		super.state(hasLeg, "*", "tiene q tener leg");

		//		if (hasLeg) {
		//			boolean allPublished = legs.stream().allMatch(leg -> !leg.isDraftMode());
		//			this.state(allPublished, "*", "las legs tienen q estar publicadas");
		//		}
	}

	@Override
	public void perform(final Flight flight) {
		flight.setDraftMode(false);
		this.repository.save(flight);
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
