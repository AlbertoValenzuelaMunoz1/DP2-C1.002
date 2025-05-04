
package acme.features.customer.bookingRecord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.student2.booking.Booking;
import acme.entities.student2.booking.BookingRecord;
import acme.entities.student2.customer.Customer;
import acme.entities.student2.passenger.Passenger;

@GuiService
public class CustomerAddPassengerToBookingService extends AbstractGuiService<Customer, BookingRecord> {

	@Autowired
	private CustomerBookingRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int customerId = this.getRequest().getPrincipal().getActiveRealm().getId();
		int bookingId = super.getRequest().getData("bookingId", int.class);
		Customer customer = this.repository.findCustomerById(customerId);
		Booking booking = this.repository.findBookingById(bookingId);
		boolean validPassenger = true;
		if (super.getRequest().getMethod().equals("POST")) {

			int passengerId = super.getRequest().getData("passenger", int.class);
			if (passengerId != 0) {
				Passenger passenger = this.repository.findPassengerById(passengerId);
				validPassenger = passenger != null && passenger.getCustomer().equals(customer) && this.repository.findBookingRecordByBookingId(bookingId).stream().noneMatch(r -> r.getPassenger().equals(passenger));
			}
		}

		super.getResponse().setAuthorised(validPassenger && booking != null && booking.getCustomer().equals(customer) && !booking.isPublished());

	}

	@Override
	public void load() {
		int bookingId = super.getRequest().getData("bookingId", int.class);
		BookingRecord bookingRecord = new BookingRecord();
		Booking booking = this.repository.findBookingById(bookingId);
		bookingRecord.setBooking(booking);
		super.getBuffer().addData(bookingRecord);
	}

	@Override
	public void bind(final BookingRecord bookingRecord) {
		super.bindObject(bookingRecord, "passenger");
	}

	@Override
	public void validate(final BookingRecord bookingRecord) {

	}

	@Override
	public void perform(final BookingRecord bookingRecord) {
		this.repository.save(bookingRecord);

	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {

		int customerId = this.getRequest().getPrincipal().getActiveRealm().getId();
		int bookingId = super.getRequest().getData("bookingId", int.class);
		List<Passenger> passengers = this.repository.findAvailablePassengersForBooking(customerId, bookingId);
		SelectChoices choices = SelectChoices.from(passengers, "fullName", bookingRecord.getPassenger());
		Dataset dataset = super.unbindObject(bookingRecord, "booking", "passenger");
		dataset.put("choices", choices);
		dataset.put("published", bookingRecord.getBooking().isPublished());
		dataset.put("bookingId", bookingRecord.getBooking().getId());
		super.getResponse().addData(dataset);
	}
}
