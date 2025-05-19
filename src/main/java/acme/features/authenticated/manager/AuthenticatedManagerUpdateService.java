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

package acme.features.authenticated.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.group.airline.Airline;
import acme.realms.Manager;

@GuiService
public class AuthenticatedManagerUpdateService extends AbstractGuiService<Authenticated, Manager> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedManagerRepository repository;

	// AbstractService<Authenticated, Consumer> ---------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(Manager.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Manager object;
		int userAccountId;
		userAccountId = super.getRequest().getPrincipal().getAccountId();
		object = this.repository.findManagerByUserAccountId(userAccountId);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Manager object) {
		assert object != null;

		super.bindObject(object, "identifier", "yearsOfExperience", "birthDate", "link", "airline");
	}

	@Override
	public void validate(final Manager object) {
		assert object != null;
		Manager managerSameIdentifier = this.repository.findManagerByIdentifier(object.getIdentifier(), object.getId());
		super.state(managerSameIdentifier == null, "identifier", "acme.validation.manager.identifier.message");
	}

	@Override
	public void perform(final Manager object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Manager object) {
		Dataset dataset;
		List<Airline> airlines;
		SelectChoices choicesAirlines;

		airlines = this.repository.findAirlines();
		choicesAirlines = SelectChoices.from(airlines, "name", object.getAirline());

		dataset = super.unbindObject(object, "identifier", "yearsOfExperience", "birthDate", "link", "airline");
		dataset.put("airlines", choicesAirlines);
		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
