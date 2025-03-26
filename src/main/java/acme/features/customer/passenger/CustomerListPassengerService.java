
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.student2.customer.Customer;
import acme.entities.student2.passenger.Passenger;

@GuiService
public class CustomerListPassengerService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	public CustomerPassengerRepository passengerRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}
	@Override
	public void load() {
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		super.getBuffer().addData(this.passengerRepository.findCustomerPassengers(customerId));
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;
		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "published");
		super.getResponse().addData(dataset);
	}
}
