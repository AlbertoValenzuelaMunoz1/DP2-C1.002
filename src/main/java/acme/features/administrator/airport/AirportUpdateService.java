
package acme.features.administrator.airport;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.OperationalScope;
import acme.entities.group.airport.Airport;

@GuiService
public class AirportUpdateService extends AbstractGuiService<Administrator, Airport> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AirportRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Airport airport;
		int id;

		id = super.getRequest().getData("id", int.class);
		airport = this.repository.findOneById(id);

		super.getBuffer().addData(airport);
	}

	@Override
	public void bind(final Airport airport) {
		super.bindObject(airport, "name", "iataCode", "operationalScope", "city", "country", "website", "email", "address", "contactPhoneNumber");
	}

	@Override
	public void validate(final Airport airport) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Airport airport) {
		this.repository.save(airport);
	}

	@Override
	public void unbind(final Airport airport) {
		Dataset dataset;
		SelectChoices choices;
		dataset = super.unbindObject(airport, "name", "iataCode", "operationalScope", "city", "country", "website", "email", "address", "contactPhoneNumber");
		choices = SelectChoices.from(OperationalScope.class, airport.getOperationalScope());
		dataset.put("confirmation", false);
		dataset.put("operationalScope", choices);

		super.getResponse().addData(dataset);
	}
}
