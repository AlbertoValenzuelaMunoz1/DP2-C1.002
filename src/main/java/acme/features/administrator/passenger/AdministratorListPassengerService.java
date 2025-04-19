
package acme.features.administrator.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.student2.booking.Booking;
import acme.entities.student2.passenger.Passenger;

@GuiService
public class AdministratorListPassengerService extends AbstractGuiService<Administrator, Passenger> {

	@Autowired
	public AdministratorPassengerRepository repository;


	@Override
	public void authorise() {
		int bookingId = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.repository.findBookingById(bookingId);
		super.getResponse().setAuthorised(booking.isPublished());
	}
	@Override
	public void load() {
		int bookingId = super.getRequest().getData("bookingId", int.class);
		super.getBuffer().addData(this.repository.findPassengersByBookingId(bookingId));
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;
		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "published");
		super.getResponse().addData(dataset);
	}
}
