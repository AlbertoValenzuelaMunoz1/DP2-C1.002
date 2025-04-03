
package acme.features.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.group.service.Service;

@GuiController
public class ServiceController extends AbstractGuiController<Any, Service> {

	@Autowired
	private ServiceListService	listService;

	@Autowired
	private ServiceShowService	showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);

	}
}
