
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.AssignmentStatus;
import acme.datatypes.AvailabilityStatus;
import acme.datatypes.FlightDuty;
import acme.entities.student1.leg.Leg;
import acme.entities.student3.flightAssignment.FlightAssignment;
import acme.entities.student3.flightCrewMember.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentPublishService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		FlightAssignment assignment;

		assignment = new FlightAssignment();

		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		Integer legId;
		Leg leg;
		Integer memberId;
		FlightCrewMember member;

		legId = super.getRequest().getData("flightLeg", int.class);
		leg = this.repository.findLegById(legId);
		memberId = super.getRequest().getData("member", int.class);
		member = this.repository.findFlightCrewMemberById(memberId);

		super.bindObject(assignment, "duty", "status", "remarks");
		assignment.setFlightLeg(leg);
		assignment.setFlightCrewMember(member);
		assignment.setLastUpdate(MomentHelper.getCurrentMoment());
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		this.validateLegCompatibility(assignment);
		this.validateDutyAssignment(assignment);
		this.validateStatusAvailability(assignment.getFlightCrewMember());
		this.validateLegHasNotOccurred(assignment.getFlightLeg());
	}

	private void validateLegHasNotOccurred(final Leg leg) {
		Date scheduledArrival = leg.getScheduledArrival();
		Date now = MomentHelper.getCurrentMoment();
		boolean hasOccurred = now.after(scheduledArrival);
		if (hasOccurred)
			super.state(false, "*", "acme.validation.flight-assignment.leg-has-occurred.message");

	}

	private void validateStatusAvailability(final FlightCrewMember member) {
		AvailabilityStatus status = member.getAvailabilityStatus();
		AvailabilityStatus requiredStatus = AvailabilityStatus.AVAILABLE;
		boolean available = status.equals(requiredStatus);
		if (!available)
			super.state(false, "*", "acme.validation.flight-assignment.member-not-available.message");
	}

	private void validateLegCompatibility(final FlightAssignment assignment) {
		Collection<Leg> existingLegs = this.repository.findLegsByFlightCrewMemberId(assignment.getFlightCrewMember().getId());

		boolean hasIncompatibleLeg = existingLegs.stream().anyMatch(existingLeg -> this.legIsNotOverlapping(assignment.getFlightLeg(), existingLeg));

		if (hasIncompatibleLeg)
			super.state(false, "*", "acme.validation.flight-assignment.member-with-overlapping-legs.message");
	}

	private void validateDutyAssignment(final FlightAssignment flightAssignment) {
		Collection<FlightAssignment> assignedDuties = this.repository.findFlightAssignmentByLegId(flightAssignment.getFlightLeg().getId());

		boolean legWithCopilot = assignedDuties.stream().anyMatch(assignment -> assignment.getDuty().equals(FlightDuty.CO_PILOT));
		boolean legWithPilot = assignedDuties.stream().anyMatch(assignment -> assignment.getDuty().equals(FlightDuty.PILOT));

		super.state(!(flightAssignment.getDuty().equals(FlightDuty.PILOT) && legWithPilot), "*", "acme.validation.flight-assignment.leg-has-pilot.message");
		super.state(!(flightAssignment.getDuty().equals(FlightDuty.CO_PILOT) && legWithCopilot), "*", "acme.validation.flight-assignment.leg-has-copilot.message");
	}

	private boolean legIsNotOverlapping(final Leg newLeg, final Leg existingLeg) {
		boolean isDepartureOverlapping = MomentHelper.isInRange(newLeg.getScheduledDeparture(), existingLeg.getScheduledDeparture(), existingLeg.getScheduledArrival());
		boolean isArrivalOverlapping = MomentHelper.isInRange(newLeg.getScheduledArrival(), existingLeg.getScheduledDeparture(), existingLeg.getScheduledArrival());
		return isDepartureOverlapping && isArrivalOverlapping;
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		assignment.setDraftMode(false);
		this.repository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;
		SelectChoices statuses;
		SelectChoices duties;
		Collection<Leg> legs;
		SelectChoices selectedLegs;
		Collection<FlightCrewMember> members;
		SelectChoices selectedMembers;

		legs = this.repository.findAllLegs();
		members = this.repository.findAllFlightCrewMembers();

		statuses = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());
		duties = SelectChoices.from(FlightDuty.class, assignment.getDuty());
		selectedLegs = SelectChoices.from(legs, "flightNumberDigits", assignment.getFlightLeg());
		selectedMembers = SelectChoices.from(members, "employeeCode", assignment.getFlightCrewMember());

		dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks", "draftMode");
		dataset.put("statuses", statuses);
		dataset.put("duties", duties);
		dataset.put("leg", selectedLegs.getSelected().getKey());
		dataset.put("legs", selectedLegs);
		dataset.put("member", selectedMembers.getSelected().getKey());
		dataset.put("members", selectedMembers);

		super.getResponse().addData(dataset);
	}
}
