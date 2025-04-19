
package acme.features.administrator.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.student2.passenger.Passenger;

@GuiService
public class AdministratorShowPassengerService extends AbstractGuiService<Administrator, Passenger> {

	@Autowired
	public AdministratorPassengerRepository repository;


	@Override
	public void authorise() {
		int passengerId = super.getRequest().getData("id", int.class);
		Passenger passenger = this.repository.findPassengerById(passengerId);
		super.getResponse().setAuthorised(passenger.isPublished());
	}
	@Override
	public void load() {
		int passengerId = super.getRequest().getData("id", int.class);
		super.getBuffer().addData(this.repository.findPassengerById(passengerId));
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;
		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds");
		super.getResponse().addData(dataset);
	}
}
