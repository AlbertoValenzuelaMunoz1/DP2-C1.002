
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
public class FlightCrewMemberActivityLogCreateService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		ActivityLog log;

		int masterId;
		FlightAssignment flightAssignment;

		masterId = super.getRequest().getData("masterId", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(masterId);

		log = new ActivityLog();
		log.setDraftMode(true);
		log.setFlightAssignment(flightAssignment);

		super.getBuffer().addData(log);
	}

	@Override
	public void bind(final ActivityLog log) {
		Date now;
		int assignmentId;
		FlightAssignment assignment;

		assignmentId = super.getRequest().getData("masterId", int.class);
		assignment = this.repository.findFlightAssignmentById(assignmentId);
		now = MomentHelper.getCurrentMoment();

		super.bindObject(log, "incidentType", "description", "severity");
		log.setRegistrationMoment(now);
		log.setFlightAssignment(assignment);
	}

	@Override
	public void validate(final ActivityLog log) {
		Date now = MomentHelper.getCurrentMoment();
		if (MomentHelper.isBefore(now, log.getFlightAssignment().getFlightLeg().getScheduledArrival()))
			super.state(false, "*", "El momento de registro del registro debe ocurrir después de que termine la escala");
		;
	}

	@Override
	public void perform(final ActivityLog log) {
		this.repository.save(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;
		SelectChoices selectedAssignments;
		Collection<FlightAssignment> assignments;
		FlightCrewMember member;

		member = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		assignments = this.repository.findFlightAssignmentsByMemberId(member.getId());
		selectedAssignments = SelectChoices.from(assignments, "leg.flightNumber", log.getFlightAssignment());

		dataset = super.unbindObject(log, "registrationMoment", "incidentType", "description", "severity", "draftMode");
		dataset.put("assignments", selectedAssignments);
		dataset.put("assignment", selectedAssignments.getSelected().getKey());
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));

		super.getResponse().addData(dataset);
	}
}
