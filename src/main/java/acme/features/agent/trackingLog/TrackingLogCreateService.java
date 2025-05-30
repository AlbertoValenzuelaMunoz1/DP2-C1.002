
package acme.features.agent.trackingLog;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Principal;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.IndicatorStatus;
import acme.entities.student4.claim.Claim;
import acme.entities.student4.tranckingLog.TrackingLog;
import acme.features.agent.claim.ClaimRepository;
import acme.realms.AssistanceAgent;

@GuiService
public class TrackingLogCreateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private TrackingLogRepository	repository;
	@Autowired
	private ClaimRepository			repository2;


	@Override
	public void authorise() {
		boolean status;
		boolean statusC = true;
		int claimId;
		int currentAssistanceAgentId;
		Claim claim;
		Principal principal;

		principal = super.getRequest().getPrincipal();

		currentAssistanceAgentId = principal.getActiveRealm().getId();
		claimId = super.getRequest().getData("claimId", int.class);
		claim = this.repository.findClaimById(claimId);
		Integer TlogId = super.getRequest().getData("id", int.class, 0);

		status = principal.hasRealmOfType(AssistanceAgent.class) && claim.getAssistanceAgent().getId() == currentAssistanceAgentId && (super.getRequest().getMethod().equals("GET") || TlogId == 0);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int claimId;
		TrackingLog trackingLog;
		Claim claim;
		Date currentMoment;

		claimId = super.getRequest().getData("claimId", int.class);
		claim = this.repository.findClaimById(claimId);

		currentMoment = MomentHelper.getCurrentMoment();

		trackingLog = new TrackingLog();
		trackingLog.setClaim(claim);
		trackingLog.setLastUpdateMoment(currentMoment);
		trackingLog.setDraftMode(true);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final TrackingLog trackingLog) {
		super.bindObject(trackingLog, "stepUndergoing", "resolutionPercentage", "claimStatus", "resolutionDetails");
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		boolean confirmation;
		int claimId = trackingLog.getClaim().getId();
		Double maxExisting = this.repository.findMaxResolutionPercentageByClaimId(claimId);

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
		super.state(trackingLog.getResolutionPercentage() != null, "resolutionPercentage", "javax.validation.constraints.NotNull.message");

		if (trackingLog.getResolutionPercentage() != null && trackingLog.getResolutionPercentage() == 100.00) {
			int existingCount = this.repository.countFullyResolvedLogs(claimId);
			super.state(existingCount < 2, "resolutionPercentage", "acme.validation.trackingLog.limit-100.message");
		}

		if (trackingLog.getResolutionPercentage() != null && maxExisting != null) {
			boolean validPercentage = trackingLog.getResolutionPercentage() >= maxExisting;
			super.state(validPercentage, "resolutionPercentage", "acme.validation.trackingLog.strict-increase.message");
		}
	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {

		Dataset dataset;

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "stepUndergoing", "resolutionPercentage", "resolutionDetails");
		if (trackingLog.getClaim() != null)
			dataset.put("claimDraftMode", trackingLog.getClaim().isDraftMode());

		SelectChoices statusChoices = SelectChoices.from(IndicatorStatus.class, trackingLog.getClaimStatus());
		dataset.put("claimStatus", statusChoices);
		dataset.put("claimId", trackingLog.getClaim().getId());

		super.getResponse().addData(dataset);
	}
}
