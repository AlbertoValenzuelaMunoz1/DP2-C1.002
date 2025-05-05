
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.controllers.GuiController;
import acme.client.services.AbstractGuiService;
import acme.entities.student2.customer.Customer;
import acme.entities.student2.passenger.Passenger;

@GuiController
public class CustomerPublishPassengerService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository repository;


	@Override
	public void authorise() {
		int customerId = this.getRequest().getPrincipal().getActiveRealm().getId();
		Customer customer = this.repository.findCustomerById(customerId);
		int passengerId = super.getRequest().getData("id", int.class);
		Passenger passenger = this.repository.findPassengerById(passengerId);
		super.getResponse().setAuthorised(passenger != null && !passenger.isPublished() && passenger.getCustomer().equals(customer));
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Passenger passenger = this.repository.findPassengerById(id);
		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {
		super.bindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds");
	}

	@Override
	public void validate(final Passenger passenger) {
		boolean confirmation;
		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Passenger passenger) {
		passenger.setPublished(true);
		this.repository.save(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {

		Dataset dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds");
		dataset.put("readonly", passenger.isPublished());
		dataset.put("confirmation", false);

		super.getResponse().addData(dataset);
	}
}
