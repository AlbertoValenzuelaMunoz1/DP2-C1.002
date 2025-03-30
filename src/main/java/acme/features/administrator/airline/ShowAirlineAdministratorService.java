
package acme.features.administrator.airline;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.AirlineType;
import acme.entities.group.airline.Airline;

@GuiService
public class ShowAirlineAdministratorService extends AbstractGuiService<Administrator, Airline> {

	@Autowired
	private AirlineAdministratorRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Airline airine = this.repository.findAirlineById(id);
		super.getBuffer().addData(airine);
	}

	@Override
	public void unbind(final Airline airline) {
		Dataset dataset;
		SelectChoices choices = SelectChoices.from(AirlineType.class, airline.getType());
		dataset = super.unbindObject(airline, "name", "iataCode", "website", "foundationMoment", "type", "email", "phoneNumber");
		dataset.put("choices", choices);
		super.getResponse().addData(dataset);
	}

}
