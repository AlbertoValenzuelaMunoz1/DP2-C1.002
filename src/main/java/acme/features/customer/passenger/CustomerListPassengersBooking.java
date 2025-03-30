
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.student2.booking.Booking;
import acme.entities.student2.customer.Customer;
import acme.entities.student2.passenger.Passenger;

@GuiService
public class CustomerListPassengersBooking extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	public CustomerPassengerRepository passengerRepository;


	@Override
	public void authorise() {
		int bookingId = super.getRequest().getData("bookingId", int.class);
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Customer customer = this.passengerRepository.findCustomerById(customerId);
		Booking booking = this.passengerRepository.findBookingById(bookingId);
		super.getResponse().setAuthorised(booking.getCustomer().equals(customer));
	}
	@Override
	public void load() {
		int bookingId = super.getRequest().getData("bookingId", int.class);
		Collection<Passenger> passengers = this.passengerRepository.findPassengersByBookingId(bookingId);
		super.getBuffer().addData(passengers);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;
		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "published");
		int bookingId = super.getRequest().getData("bookingId", int.class);
		dataset.put("bookingId", bookingId);
		super.getResponse().addData(dataset);
	}
}
