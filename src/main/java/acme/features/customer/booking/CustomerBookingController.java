
package acme.features.customer.booking;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.student2.booking.Booking;
import acme.entities.student2.customer.Customer;

@GuiController
public class CustomerBookingController extends AbstractGuiController<Customer, Booking> {

	@Autowired
	private CustomerListBookingService		listService;

	@Autowired
	private CustomerShowBookingService		showService;
	@Autowired
	private CustomerCreateBookingService	createService;
	@Autowired
	private CustomerUpdateBookingService	updateService;
	@Autowired
	private CustomerPublishBookingService	publishService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
