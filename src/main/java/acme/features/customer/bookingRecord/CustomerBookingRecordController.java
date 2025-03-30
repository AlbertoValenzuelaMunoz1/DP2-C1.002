
package acme.features.customer.bookingRecord;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.student2.booking.BookingRecord;
import acme.entities.student2.customer.Customer;

@GuiController
public class CustomerBookingRecordController extends AbstractGuiController<Customer, BookingRecord> {

	@Autowired
	private CustomerAddPassengerToBookingService addToBooking;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.addToBooking);
	}
}
