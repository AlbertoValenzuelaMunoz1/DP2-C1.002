
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.student2.booking.Booking;
import acme.entities.student2.customer.Customer;

@GuiService
public class CustomerListBookingService extends AbstractGuiService<Customer, Booking> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<Booking> booking = this.repository.findBookingsByCustomerId(customerId);

		super.getBuffer().addData(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		Money price = booking.price();
		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastNibble");
		dataset.put("price", price);

		super.getResponse().addData(dataset);
	}

}
