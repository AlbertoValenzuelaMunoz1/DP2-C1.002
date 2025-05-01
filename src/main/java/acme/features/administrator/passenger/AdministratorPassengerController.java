
package acme.features.administrator.passenger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.student2.passenger.Passenger;

@GuiController
public class AdministratorPassengerController extends AbstractGuiController<Administrator, Passenger> {

	@Autowired
	private AdministratorListPassengerService	listService;

	@Autowired
	private AdministratorShowPassengerService	showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}

}
