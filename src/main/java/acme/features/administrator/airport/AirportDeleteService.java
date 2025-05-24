
package acme.features.administrator.airport;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.OperationalScope;
import acme.entities.group.airport.Airport;

// INUTIL EN PRINCIPIO

@GuiService
public class AirportDeleteService extends AbstractGuiService<Administrator, Airport> {

	@Autowired
	private AirportRepository repository;


	@Override
	public void authorise() {

		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Airport airport = this.repository.findOneById(id);
		super.getBuffer().addData(airport);
	}

	@Override
	public void bind(final Airport airport) {
		super.bindObject(airport, "name", "iataCode", "operationalScope", "city", "country", "website", "email", "address", "contactPhoneNumber");
	}

	@Override
	public void validate(final Airport airport) {

	}

	@Override
	public void perform(final Airport airport) {
		this.repository.delete(airport);
	}

	@Override
	public void unbind(final Airport airport) {
		Dataset dataset;
		SelectChoices choices;
		dataset = super.unbindObject(airport, "name", "iataCode", "operationalScope", "city", "country", "website", "email", "address", "contactPhoneNumber");
		choices = SelectChoices.from(OperationalScope.class, airport.getOperationalScope());
		dataset.put("operationalScope", choices);
		super.getResponse().addData(dataset);
	}

}
