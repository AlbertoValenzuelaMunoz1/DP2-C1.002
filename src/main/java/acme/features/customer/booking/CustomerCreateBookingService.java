
package acme.features.customer.booking;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.TravelClass;
import acme.entities.student1.flight.Flight;
import acme.entities.student2.booking.Booking;
import acme.entities.student2.customer.Customer;

@GuiService
public class CustomerCreateBookingService extends AbstractGuiService<Customer, Booking> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean validFlight = true;
		if (super.getRequest().getMethod().equals("POST")) {
			int flightId = super.getRequest().getData("flight", int.class);
			if (flightId != 0) {
				Flight flight = this.repository.findFlightById(flightId);
				validFlight = flight != null && !flight.isDraftMode() && flight.scheduledDeparture().after(MomentHelper.getCurrentMoment());
			}

		}
		super.getResponse().setAuthorised(validFlight);
	}

	@Override
	public void load() {
		Booking booking = new Booking();
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Customer customer = this.repository.findCustomerById(customerId);
		booking.setCustomer(customer);
		booking.setPurchaseMoment(MomentHelper.getCurrentMoment());
		super.getBuffer().addData(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		SelectChoices choices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		SelectChoices choicesFlight = SelectChoices.from(this.repository.findAllPublishedFutureFlights(MomentHelper.getCurrentMoment()), "tag", booking.getFlight());
		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "lastNibble");
		dataset.put("choices", choices);
		dataset.put("choicesFlight", choicesFlight);
		dataset.put("travelClass", choices.getSelected().getKey());
		dataset.put("flight", choicesFlight.getSelected().getKey());
		dataset.put("bookingId", booking.getId());
		super.getResponse().addData(dataset);
	}
	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "locatorCode", "lastNibble", "flight", "travelClass");
	}
	@Override
	public void perform(final Booking booking) {
		this.repository.save(booking);
	}
	@Override
	public void validate(final Booking booking) {
		boolean confirmation;
		boolean uniqueLocatorCode;
		confirmation = super.getRequest().getData("confirmation", boolean.class);
		uniqueLocatorCode = this.repository.findByLocatorCode(booking.getLocatorCode()) == null;
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
		super.state(uniqueLocatorCode, "locatorCode", "acme.validation.booking.locatorCode.message");
	}

}
