
package acme.features.administrator.airline;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.group.airline.Airline;

@GuiController
public class AirlineAdministratorController extends AbstractGuiController<Administrator, Airline> {

	@Autowired
	private ListAirlineAdministratorService		listService;

	@Autowired
	private ShowAirlineAdministratorService		showService;
	@Autowired
	private CreateAirlineAdministratorService	createService;
	@Autowired
	private UpdateAirlineAdministratorService	updateService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
	}

}
