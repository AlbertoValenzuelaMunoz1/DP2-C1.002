
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
public class CreateAirlineAdministratorService extends AbstractGuiService<Administrator, Airline> {

	@Autowired
	private AirlineAdministratorRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Airline airine = new Airline();
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
	@Override
	public void bind(final Airline airline) {
		super.bindObject(airline, "name", "iataCode", "website", "foundationMoment", "type", "email", "phoneNumber");
	}
	@Override
	public void perform(final Airline airline) {
		this.repository.save(airline);
	}
	@Override
	public void validate(final Airline airline) {
		boolean confirmation;
		boolean uniqueIataCode;
		confirmation = super.getRequest().getData("confirmation", boolean.class);
		uniqueIataCode = this.repository.findAirlineByIataCode(airline.getIataCode()) == null;
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
		super.state(uniqueIataCode, "iataCode", "acme.validation.airline.iataCode.message");
	}

}
