
package acme.features.agent.trackingLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Principal;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.student4.claim.Claim;
import acme.entities.student4.tranckingLog.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class TrackingLogListService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	//-----------------------------------------------------------------------

	@Autowired
	protected TrackingLogRepository repository;

	//------------------------------------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int claimId;
		int currentAssistanceAgentId;
		Claim claim;
		Principal principal;

		principal = super.getRequest().getPrincipal();

		currentAssistanceAgentId = principal.getActiveRealm().getId();
		claimId = super.getRequest().getData("claimId", int.class);
		claim = this.repository.findClaimById(claimId);

		status = principal.hasRealmOfType(AssistanceAgent.class) && claim.getAssistanceAgent().getId() == currentAssistanceAgentId;

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		int claimId;
		Collection<TrackingLog> trackingLogs;

		claimId = super.getRequest().getData("claimId", int.class);

		trackingLogs = this.repository.findAllLogsFromClaim(claimId);
		super.getBuffer().addData(trackingLogs);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "stepUndergoing", "resolutionPercentage", "claimStatus", "resolutionDetails");

		super.getResponse().addData(dataset);

	}

	@Override
	public void unbind(final Collection<TrackingLog> objects) {
		int claimId;

		claimId = super.getRequest().getData("claimId", int.class);

		super.getResponse().addGlobal("claimId", claimId);
	}

}
