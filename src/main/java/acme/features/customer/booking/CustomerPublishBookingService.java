
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.StringHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.TravelClass;
import acme.entities.student1.flight.Flight;
import acme.entities.student2.booking.Booking;
import acme.entities.student2.customer.Customer;

@GuiService
public class CustomerPublishBookingService extends AbstractGuiService<Customer, Booking> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int bookingId = super.getRequest().getData("id", int.class);
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Customer customer = this.repository.findCustomerById(customerId);
		Booking booking = this.repository.findBookingById(bookingId);
		boolean validFlight = true;
		if (super.getRequest().getMethod().equals("POST")) {
			int flightId = super.getRequest().getData("flight", int.class);
			if (flightId != 0) {
				Flight flight = this.repository.findFlightById(flightId);
				validFlight = flight != null && (booking != null && booking.getFlight().getId() == flightId || !flight.isDraftMode() && flight.scheduledDeparture().after(MomentHelper.getCurrentMoment()));
			}

		}
		super.getResponse().setAuthorised(booking != null && validFlight && customer.equals(booking.getCustomer()) && !booking.isPublished());
	}

	@Override
	public void load() {
		Booking booking;
		int id;
		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(id);
		booking.setPurchaseMoment(MomentHelper.getCurrentMoment());
		super.getBuffer().addData(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		Money price = booking.price();
		SelectChoices choices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		Collection<Flight> futureFlights = this.repository.findAllPublishedFutureFlights(MomentHelper.getCurrentMoment());
		if (booking.getFlight() != null && !futureFlights.contains(booking.getFlight()))
			futureFlights.add(booking.getFlight());
		SelectChoices choicesFlight = SelectChoices.from(futureFlights, "tag", booking.getFlight());
		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "lastNibble");
		dataset.put("price", price);
		dataset.put("choices", choices);
		dataset.put("choicesFlight", choicesFlight);
		dataset.put("travelClass", choices.getSelected().getKey());
		dataset.put("flight", choicesFlight.getSelected().getKey());
		dataset.put("bookingId", booking.getId());
		dataset.put("readonly", booking.isPublished());
		super.getResponse().addData(dataset);
	}
	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "locatorCode", "lastNibble", "flight", "travelClass");
	}
	@Override
	public void perform(final Booking booking) {
		booking.setPublished(true);
		this.repository.save(booking);
	}
	@Override
	public void validate(final Booking booking) {
		boolean confirmation;
		boolean valid;
		boolean uniqueLocatorCode;
		boolean allPassengersPublished;
		boolean validFlight;
		confirmation = super.getRequest().getData("confirmation", boolean.class);
		valid = booking.getLastNibble() != null && !StringHelper.isBlank(booking.getLastNibble()) && !booking.passengers().isEmpty();
		uniqueLocatorCode = this.repository.findByLocatorCode(booking.getLocatorCode(), booking.getId()) == null;
		allPassengersPublished = this.repository.findBookingRecordByBookingId(booking.getId()).stream().allMatch(r -> r.getPassenger().isPublished());
		validFlight = booking.getFlight() == null || booking.getFlight().scheduledDeparture().after(MomentHelper.getCurrentMoment());
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
		super.state(valid, "confirmation", "acme.validation.booking.published.message");
		super.state(allPassengersPublished, "*", "acme.validation.booking.publishedPassengers.message");
		super.state(uniqueLocatorCode, "locatorCode", "acme.validation.booking.locatorCode.message");
		super.state(validFlight, "flight", "acme.validation.booking.flight.message");
	}
}
