
package acme.features.agent.claim;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.student1.leg.Leg;
import acme.entities.student4.claim.Claim;
import acme.realms.AssistanceAgent;

@GuiService
public class ClaimDeleteService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private ClaimRepository repository;


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
		if (claim.isDraftMode())
			super.state(claim.isDraftMode(), "draftMode", "assistanceAgent.claim.form.error.draftMode");
	}

	@Override
	public void perform(final Claim claim) {
		this.repository.delete(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "indicator");

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
