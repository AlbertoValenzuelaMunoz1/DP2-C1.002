
package acme.features.agent.claim;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.student4.claim.Claim;
import acme.realms.AssistanceAgent;

@GuiService
public class ClaimUpdateService extends AbstractGuiService<AssistanceAgent, Claim> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private ClaimRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Claim claim;
		int id;

		id = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(id);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		super.bindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "indicator");
	}

	@Override
	public void validate(final Claim claim) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Claim claim) {
		this.repository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "indicator");
		dataset.put("confirmation", false);

		super.getResponse().addData(dataset);
	}
}
