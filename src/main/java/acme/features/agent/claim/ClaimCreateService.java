
package acme.features.agent.claim;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.ClaimType;
import acme.entities.student1.leg.Leg;
import acme.entities.student4.claim.Claim;
import acme.realms.AssistanceAgent;

@GuiService
public class ClaimCreateService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private ClaimRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		AssistanceAgent agent;
		Date registrationMoment;

		agent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();
		registrationMoment = MomentHelper.getCurrentMoment();
		claim = new Claim();
		claim.setRegistrationMoment(registrationMoment);
		claim.setAssistanceAgent(agent);
		claim.setDraftMode(true);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		int legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		claim.setLeg(leg);
		super.bindObject(claim, "passengerEmail", "description", "type");
	}

	@Override
	public void validate(final Claim claim) {
	}

	@Override
	public void perform(final Claim claim) {
		this.repository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		SelectChoices choices;
		Dataset dataset;
		SelectChoices choices_leg;
		Collection<Leg> legs;

		legs = this.repository.findAllPublishedCompletedLegs(claim.getRegistrationMoment());

		choices = SelectChoices.from(ClaimType.class, claim.getType());
		choices_leg = SelectChoices.from(legs, "flightNumberDigits", claim.getLeg());
		legs = this.repository.findAllPublishedCompletedLegs(claim.getRegistrationMoment());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "indicator");
		dataset.put("type", choices);
		dataset.put("legs", choices_leg);
		dataset.put("flightNumberDigits", choices_leg.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}
