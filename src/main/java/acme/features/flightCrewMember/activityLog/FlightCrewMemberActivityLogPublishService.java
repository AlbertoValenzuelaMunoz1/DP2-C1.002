
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.student3.activityLog.ActivityLog;
import acme.entities.student3.flightAssignment.FlightAssignment;
import acme.entities.student3.flightCrewMember.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogPublishService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int logId;
		FlightCrewMember member;
		ActivityLog log;

		logId = super.getRequest().getData("id", int.class);
		log = this.repository.findActivityLogById(logId);
		member = log == null ? null : log.getFlightAssignment().getFlightCrewMember();
		status = member != null && log.isDraftMode() && super.getRequest().getPrincipal().hasRealm(member);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ActivityLog log;
		int logId;

		logId = super.getRequest().getData("id", int.class);
		log = this.repository.findActivityLogById(logId);

		super.getBuffer().addData(log);
	}

	@Override
	public void bind(final ActivityLog log) {
		super.bindObject(log, "incidentType", "description", "severity");

	}

	@Override
	public void validate(final ActivityLog log) {
		FlightAssignment assignment = log.getFlightAssignment();
		Date now = MomentHelper.getCurrentMoment();
		if (assignment.isDraftMode())
			super.state(false, "*", "acme.validation.activity-log.flight-assignment-not-published.message");
		if (now.before(assignment.getFlightLeg().getScheduledArrival()))
			super.state(false, "*", "acme.validation.activity-log.leg-not-finished.message");
	}

	@Override
	public void perform(final ActivityLog log) {
		log.setDraftMode(false);
		this.repository.save(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;
		SelectChoices selectedAssignments;
		Collection<FlightAssignment> assignments;
		FlightCrewMember member;

		member = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		assignments = this.repository.findFlightAssignmentsByMemberIdAndPublished(member.getId());
		selectedAssignments = SelectChoices.from(assignments, "leg.flightNumber", log.getFlightAssignment());

		dataset = super.unbindObject(log, "registrationMoment", "incidentType", "description", "severity", "draftMode");
		dataset.put("assignments", selectedAssignments);
		dataset.put("assignment", selectedAssignments.getSelected().getKey());
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));

		super.getResponse().addData(dataset);
	}
}
