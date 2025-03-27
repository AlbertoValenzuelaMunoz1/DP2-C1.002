
package acme.features.administrator.airport;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractService;
import acme.client.services.GuiService;
import acme.entities.group.airport.Airport;

@GuiService
public class AirportListService extends AbstractService<Administrator, Airport> {

	//-----------------------------------------------------------------------

	@Autowired
	protected AirportRepository repository;

	//------------------------------------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Airport> airports;

		airports = this.repository.findAllAirports();
		super.getBuffer().addData(airports);
	}

	@Override
	public void unbind(final Airport airport) {

		Dataset dataset = super.unbindObject(airport, "name", "iataCode", "operationalScope", "city", "country", "website", "email", "address", "contactPhoneNumber");

		//super.addPayload(dataset, airport, "details");
		super.getResponse().addData(dataset);
	}
}
