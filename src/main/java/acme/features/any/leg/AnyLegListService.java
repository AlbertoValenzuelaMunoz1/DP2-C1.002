
package acme.features.any.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.student1.leg.Leg;

@GuiService
public class AnyLegListService extends AbstractGuiService<Any, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyLegRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Leg> legs;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		legs = this.repository.findLegById(masterId);

		super.getBuffer().addData(legs);

		super.getResponse().addGlobal("masterId", masterId);

	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;
		boolean showCreate;

		dataset = super.unbindObject(leg, "flightNumberDigits", "scheduledDeparture", "scheduledArrival", "departureAirport", "arrivalAirport", "status");
		super.addPayload(dataset, leg, "aircraft", "flight");

		showCreate = leg != null && leg.getFlight().isDraftMode();

		super.getResponse().addGlobal("showCreate", showCreate);
		super.getResponse().addData(dataset);
	}

}
