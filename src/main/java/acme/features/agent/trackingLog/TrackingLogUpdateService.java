
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
import acme.entities.student4.tranckingLog.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class TrackingLogUpdateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private TrackingLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int trackingLogId;
		int currentAssistanceAgentId;
		Principal principal;
		TrackingLog trackingLog;

		principal = super.getRequest().getPrincipal();

		currentAssistanceAgentId = principal.getActiveRealm().getId();
		trackingLogId = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findLogById(trackingLogId);

		status = principal.hasRealmOfType(AssistanceAgent.class) && trackingLog.getClaim().getAssistanceAgent().getId() == currentAssistanceAgentId;

		super.getResponse().setAuthorised(status);
	}
	@Override
	public void load() {
		int trackingLogId;
		TrackingLog trackingLog;

		trackingLogId = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findLogById(trackingLogId);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final TrackingLog trackingLog) {
		super.bindObject(trackingLog, "stepUndergoing", "resolutionPercentage", "claimStatus", "resolutionDetails");
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		int claimId = trackingLog.getClaim().getId();
		Double maxExisting = this.repository.findMaxResolutionPercentageByClaimId(claimId);

		if (!trackingLog.isDraftMode())
			super.state(trackingLog.isDraftMode(), "*", "assistance-agent.tracking-log.form.error.draftMode");

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
		Date currentMoment;

		currentMoment = MomentHelper.getCurrentMoment();

		trackingLog.setLastUpdateMoment(currentMoment);
		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;
		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "stepUndergoing", "resolutionPercentage", "claimStatus", "resolutionDetails", "draftMode");
		if (trackingLog.getClaim() != null)
			dataset.put("claimDraftMode", trackingLog.getClaim().isDraftMode());

		SelectChoices statusChoices = SelectChoices.from(IndicatorStatus.class, trackingLog.getClaimStatus());
		dataset.put("claimStatus", statusChoices);
		dataset.put("claimId", trackingLog.getClaim().getId());

		super.getResponse().addData(dataset);

	}

}
