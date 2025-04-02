
package acme.features.customer.bookingRecord;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.student2.booking.BookingRecord;
import acme.entities.student2.customer.Customer;

@GuiService
public class CustomerBookingRecordDeleteService extends AbstractGuiService<Customer, BookingRecord> {

	@Autowired
	public CustomerBookingRecordRepository passengerRepository;


	@Override
	public void authorise() {
		int customerId = this.getRequest().getPrincipal().getActiveRealm().getId();
		Customer customer = this.passengerRepository.findCustomerById(customerId);
		int bookingRecordId = super.getRequest().getData("id", int.class);
		BookingRecord bookingRecord = this.passengerRepository.findBookingRecordById(bookingRecordId);
		super.getResponse().setAuthorised(customer.equals(bookingRecord.getBooking().getCustomer()) && !bookingRecord.getBooking().isPublished());
		super.getResponse().setAuthorised(true);
	}
	@Override
	public void load() {
		int passengerId = super.getRequest().getData("id", int.class);
		super.getBuffer().addData(this.passengerRepository.findBookingRecordById(passengerId));
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Dataset dataset = super.unbindObject(bookingRecord, "passenger.fullName", "passenger.email", "passenger.passportNumber", "passenger.dateOfBirth", "passenger.specialNeeds");
		dataset.put("published", bookingRecord.getBooking().isPublished());
		super.getResponse().addData(dataset);
	}
	@Override
	public void perform(final BookingRecord r) {
		this.passengerRepository.delete(r);
	}
	@Override
	public void bind(final BookingRecord r) {

	}
	@Override
	public void validate(final BookingRecord r) {

	}
}
