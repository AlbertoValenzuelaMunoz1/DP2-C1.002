
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
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Claim> claim;

		claim = this.repository.findAllClaims();
		super.getBuffer().addData(claim);
	}

	@Override
	public void unbind(final Claim claim) {

		Dataset dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "indicator");

		super.getResponse().addData(dataset);
	}

}
