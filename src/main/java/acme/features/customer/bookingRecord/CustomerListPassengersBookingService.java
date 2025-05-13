
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.student2.booking.Booking;
import acme.entities.student2.booking.BookingRecord;
import acme.entities.student2.customer.Customer;

@GuiService
public class CustomerListPassengersBookingService extends AbstractGuiService<Customer, BookingRecord> {

	@Autowired
	public CustomerBookingRecordRepository passengerRepository;


	@Override
	public void authorise() {
		int bookingId = super.getRequest().getData("bookingId", int.class);
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Customer customer = this.passengerRepository.findCustomerById(customerId);
		Booking booking = this.passengerRepository.findBookingById(bookingId);
		super.getResponse().setAuthorised(booking != null && booking.getCustomer().equals(customer));
	}
	@Override
	public void load() {
		int bookingId = super.getRequest().getData("bookingId", int.class);
		Collection<BookingRecord> passengers = this.passengerRepository.findBookingRecordByBookingId(bookingId);
		super.getBuffer().addData(passengers);
		super.getResponse().addGlobal("bookingId", bookingId);
		super.getResponse().addGlobal("published", this.passengerRepository.findBookingById(bookingId).isPublished());
	}

	@Override
	public void unbind(final BookingRecord record) {
		Dataset dataset;
		dataset = super.unbindObject(record, "passenger.fullName", "passenger.email", "passenger.passportNumber", "passenger.dateOfBirth", "passenger.specialNeeds", "passenger.published");
		super.getResponse().addData(dataset);
	}
}
