/*
 * AuthenticatedConsumerUpdateService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.authenticated.customer;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.student2.customer.Customer;

@GuiService
public class AuthenticatedCustomerUpdateService extends AbstractGuiService<Authenticated, Customer> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedCustomerRepository repository;

	// AbstractService<Authenticated, Consumer> ---------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Customer object;
		int userAccountId;
		userAccountId = super.getRequest().getPrincipal().getAccountId();
		object = this.repository.findCustomerByUserAccountId(userAccountId);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Customer object) {
		assert object != null;

		super.bindObject(object, "identifier", "phoneNumber", "physicalAddres", "city", "country", "earnedPoints");
	}

	@Override
	public void validate(final Customer object) {
		assert object != null;
		Customer customerSameIdentifier = this.repository.findCustomerByIdentifier(object.getIdentifier(), object.getId());
		super.state(customerSameIdentifier == null, "identifier", "acme.validation.customer.identifier.message");
	}

	@Override
	public void perform(final Customer object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Customer object) {
		Dataset dataset;
		dataset = super.unbindObject(object, "identifier", "physicalAddres", "city", "phoneNumber", "country", "earnedPoints");
		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
