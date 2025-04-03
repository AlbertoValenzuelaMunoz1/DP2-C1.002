
package acme.features.service;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.group.service.Service;

@GuiService
public class ServiceListService extends AbstractGuiService<Any, Service> {

	@Autowired
	private ServiceRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int serviceId = super.getRequest().getData("id", int.class);
		Service service = this.repository.findServiceById(serviceId);

		super.getBuffer().addData(service);
	}

	@Override
	public void unbind(final Service service) {
		Dataset dataset = super.unbindObject(service, "name", "picture", "description", "averageDwellTime", "promotionCode", "money");
		super.getResponse().addData(dataset);
	}
}
