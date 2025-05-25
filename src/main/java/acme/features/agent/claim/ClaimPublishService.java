
package acme.features.agent.claim;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.ClaimType;
import acme.entities.student1.leg.Leg;
import acme.entities.student4.claim.Claim;
import acme.realms.AssistanceAgent;

@GuiService
public class ClaimPublishService extends AbstractGuiService<AssistanceAgent, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClaimRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		boolean statusLeg = true;
		boolean nullStatus = true;
		int claimId = super.getRequest().getData("id", int.class);
		int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Claim claim = this.repository.findClaimById(claimId);
		AssistanceAgent agent = this.repository.findAgentById(agentId);
		Date Moment;
		if (claim != null) {
			Moment = claim.getRegistrationMoment();
			Leg leg = null;

			Collection<Leg> legs = this.repository.findAllPublishedCompletedLegs(Moment);
			leg = this.getLeg(claim);
			statusLeg = leg != null ? legs.contains(leg) : nullStatus;
		}

		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class) && claim != null && claim.getAssistanceAgent().equals(agent) && claim.isDraftMode() && statusLeg
			&& claim.getVersion() == super.getRequest().getData("version", int.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id;
		Claim claim;

		id = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(id);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		int legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		claim.setLeg(leg);
		super.bindObject(claim, "passengerEmail", "description", "type", "indicator");
	}

	@Override
	public void validate(final Claim claim) {

	}

	@Override
	public void perform(final Claim claim) {
		claim.setDraftMode(false);
		this.repository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		SelectChoices choices_type;
		SelectChoices choices_leg;
		Dataset dataset;
		Collection<Leg> legs;

		legs = this.repository.findAllPublishedCompletedLegs(claim.getRegistrationMoment());

		choices_type = SelectChoices.from(ClaimType.class, claim.getType());
		choices_leg = SelectChoices.from(legs, "flightNumber", claim.getLeg());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "indicator");
		dataset.put("type", choices_type);
		dataset.put("legs", choices_leg);
		dataset.put("legFlightNumber", claim.getLeg().getFlightNumberDigits());

		super.getResponse().addData(dataset);
	}

	private Leg getLeg(final Claim claim) {
		int legId;
		Leg leg;

		String method = super.getRequest().getMethod();

		if (method.equals("GET"))
			legId = claim.getLeg().getId();
		else
			legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);
		return leg;
	}

}
