
package acme.features.service;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.group.service.Service;

@GuiService
public class ServiceShowService extends AbstractGuiService<Any, Service> {

	@Autowired
	private ServiceRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Service service;
		int id;

		id = super.getRequest().getData("id", int.class);
		service = this.repository.findServiceById(id);

		super.getBuffer().addData(service);
	}

	@Override
	public void unbind(final Service service) {

		Dataset dataset;
		dataset = super.unbindObject(service, "name", "picture", "averageDwellTime", "promotionCode", "money", "description");

		super.getResponse().addData(dataset);
	}
}
