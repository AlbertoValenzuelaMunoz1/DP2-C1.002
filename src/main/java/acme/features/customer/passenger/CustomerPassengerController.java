
package acme.features.customer.passenger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.student2.customer.Customer;
import acme.entities.student2.passenger.Passenger;

@GuiController
public class CustomerPassengerController extends AbstractGuiController<Customer, Passenger> {

	@Autowired
	private CustomerListPassengerService	listService;
	@Autowired
	private CustomerShowPassengerService	showService;
	@Autowired
	private CustomerCreatePassengerService	createService;
	@Autowired
	private CustomerUpdatePassengerService	updateService;
	@Autowired
	private CustomerPublishPassengerService	publishService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addCustomCommand("publish", "update", this.publishService);
	}
}
