
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

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
public class FlightCrewMemberFlightAssignmentCreateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status;
		boolean validLeg;
		boolean isMember;
		FlightCrewMember member;

		isMember = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);

		status = isMember;

		boolean validId = true;
		boolean validFlightCrewMemberId = true;
		if (super.getRequest().getMethod().equals("POST")) {
			int legId = super.getRequest().getData("flightLeg", int.class);
			int flightCrewMemberId = super.getRequest().getData("flightCrewMember", int.class);
			FlightCrewMember flightCrewMember = this.repository.findCrewMemberById(flightCrewMemberId);
			Leg leg = this.repository.findLegById(legId);
			int id = super.getRequest().getData("id", int.class, 0);
			validId = id == 0;

			validFlightCrewMemberId = flightCrewMemberId == 0 || flightCrewMember != null;
			validLeg = legId == 0 || leg != null;
			if (validLeg && leg != null) {
				member = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
				boolean isFuture = MomentHelper.isBefore(MomentHelper.getCurrentMoment(), leg.getScheduledArrival());
				boolean isMyAirline = leg.getAircraft().getAirline().getId() == member.getAirline().getId();
				validLeg = !leg.isDraftMode() && isFuture && isMyAirline;

			}
			status = validLeg && validId;
			if (validFlightCrewMemberId && flightCrewMember != null) {
				member = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
				validFlightCrewMemberId = flightCrewMember.getAirline().equals(member.getAirline());
			}
			status = status && validFlightCrewMemberId;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment assignment;

		assignment = new FlightAssignment();
		assignment.setDraftMode(true);
		assignment.setLastUpdate(MomentHelper.getCurrentMoment());

		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		int legId = super.getRequest().getData("flightLeg", int.class);
		Leg leg = this.repository.findLegById(legId);

		int memberId = super.getRequest().getData("flightCrewMember", int.class);
		FlightCrewMember member = this.repository.findFlightCrewMemberById(memberId);

		super.bindObject(assignment, "duty", "status", "remarks");
		assignment.setFlightLeg(leg);
		assignment.setFlightCrewMember(member);
		assignment.setLastUpdate(MomentHelper.getCurrentMoment());
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		if (assignment.getFlightLeg() != null) {
			if (assignment.getFlightCrewMember() != null) {
				this.validateLegCompatibility(assignment);
				this.validateStatusAvailability(assignment.getFlightCrewMember());
			}

			if (assignment.getDuty() != null)
				this.validateDutyAssignment(assignment);

			//			this.validateLegHasNotOccurred(assignment.getFlightLeg());
		}
	}

	//	private void validateLegHasNotOccurred(final Leg leg) {
	//		Date scheduledArrival = leg.getScheduledArrival();
	//		Date now = MomentHelper.getCurrentMoment();
	//		boolean hasOccurred = MomentHelper.isAfter(now, scheduledArrival);
	//		if (hasOccurred)
	//			super.state(false, "*", "acme.validation.flight-assignment.leg-has-occurred.message");
	//
	//	}

	private void validateStatusAvailability(final FlightCrewMember member) {
		AvailabilityStatus status = member.getAvailabilityStatus();
		AvailabilityStatus requiredStatus = AvailabilityStatus.AVAILABLE;
		boolean available = status.equals(requiredStatus);
		if (!available)
			super.state(false, "*", "acme.validation.flight-assignment.member-not-available.message");
	}

	private void validateLegCompatibility(final FlightAssignment assignment) {
		Collection<Leg> existingLegs = this.repository.findLegsByFlightCrewMemberId(assignment.getFlightCrewMember().getId(), assignment.getId());

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
		return isDepartureOverlapping || isArrivalOverlapping;
	}

	@Override
	public void perform(final FlightAssignment assignment) {
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

		FlightCrewMember activeMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		legs = this.repository.findPublishedFutureOwnedLegs(MomentHelper.getCurrentMoment(), activeMember.getAirline());
		members = this.repository.findCrewMembersByAirline(activeMember.getAirline());

		statuses = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());
		duties = SelectChoices.from(FlightDuty.class, assignment.getDuty());
		selectedLegs = SelectChoices.from(legs, "flightNumberDigits", assignment.getFlightLeg());
		selectedMembers = SelectChoices.from(members, "employeeCode", assignment.getFlightCrewMember());

		dataset = super.unbindObject(assignment, "duty", "status", "remarks", "draftMode");

		dataset.put("statuses", statuses);
		dataset.put("duties", duties);
		dataset.put("flightLeg", selectedLegs.getSelected().getKey());
		dataset.put("legs", selectedLegs);
		dataset.put("flightCrewMember", selectedMembers.getSelected().getKey());
		dataset.put("crewMembers", selectedMembers);

		super.getResponse().addData(dataset);
	}

}
