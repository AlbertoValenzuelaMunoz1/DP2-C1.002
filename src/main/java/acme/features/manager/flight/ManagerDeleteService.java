
package acme.features.manager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.student1.flight.Flight;
import acme.entities.student1.leg.Leg;
import acme.realms.Manager;

@GuiService
public class ManagerDeleteService extends AbstractGuiService<Manager, Flight> {

	@Autowired
	private ManagerRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Flight flight;
		Manager manager;

		masterId = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(masterId).orElse(null);
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
		boolean status;
		boolean legsPublish;

		status = super.getRequest().getData("draftMode", boolean.class);

		Collection<Leg> legs = this.repository.findLegsByFlightId(flight.getId());

		legsPublish = legs.stream().allMatch(Leg::isDraftMode);

		super.state(legsPublish, "*", "no puedes borrar un vuelo con legs publicadas.");

		super.state(status, "draftMode", "no puedes borrar un flight publicado");

	}

	@Override
	public void perform(final Flight flight) {
		Collection<Leg> flightLegs = this.repository.findLegsByFlightId(flight.getId());

		flightLegs.stream().forEach(l -> this.repository.delete(l));

		this.repository.delete(flight);
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
