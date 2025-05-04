
package acme.features.agent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.student4.claim.Claim;
import acme.realms.AssistanceAgent;

@GuiService
public class ClaimListService extends AbstractGuiService<AssistanceAgent, Claim> {

	//-----------------------------------------------------------------------

	@Autowired
	protected ClaimRepository repository;

	//------------------------------------------------------------------------


	@Override
	public void authorise() {

		boolean status;
		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Claim> claims;
		int id;

		id = super.getRequest().getPrincipal().getActiveRealm().getId();
		claims = this.repository.findAllClaimsByAssistanceAgentId(id);

		super.getBuffer().addData(claims);
	}

	@Override
	public void unbind(final Claim claim) {

		Dataset dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "indicator", "leg");

		super.getResponse().addData(dataset);
	}

}
