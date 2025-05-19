
package acme.features.any.flight;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.student1.flight.Flight;

@GuiService
public class AnyShowService extends AbstractGuiService<Any, Flight> {

	@Autowired
	private AnyRepository repository;


	@Override
	public void authorise() {

		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Flight flight;
		int id;

		id = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(id).isPresent() ? this.repository.findFlightById(id).get() : null;

		super.getBuffer().addData(flight);
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
