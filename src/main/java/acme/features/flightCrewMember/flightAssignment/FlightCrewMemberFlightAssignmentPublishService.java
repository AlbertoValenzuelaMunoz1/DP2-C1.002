
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
public class FlightCrewMemberFlightAssignmentPublishService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status;
		boolean validLeg;
		int masterId;
		FlightAssignment assignment;
		FlightCrewMember member;

		masterId = super.getRequest().getData("id", int.class);
		assignment = this.repository.findFlightAssignmentById(masterId);
		member = assignment == null ? null : assignment.getFlightCrewMember();
		status = assignment != null && assignment.isDraftMode() && super.getRequest().getPrincipal().hasRealm(member);

		boolean validFlightCrewMemberId = true;
		if (super.getRequest().getMethod().equals("POST") && status) {
			int legId = super.getRequest().getData("flightLeg", int.class);
			Leg leg = this.repository.findLegById(legId);
			int flightCrewMemberId = super.getRequest().getData("flightCrewMember", int.class);
			FlightCrewMember flightCrewMember = this.repository.findCrewMemberById(flightCrewMemberId);

			validFlightCrewMemberId = flightCrewMemberId == 0 || flightCrewMember != null;
			validLeg = legId == 0 || leg != null;
			if (validLeg && leg != null) {
				boolean isSameAsAssigned = assignment.getFlightLeg() != null && leg.getId() == assignment.getFlightLeg().getId();
				boolean isFuture = MomentHelper.isBefore(MomentHelper.getCurrentMoment(), leg.getScheduledArrival());
				boolean isMyAirline = leg.getAircraft().getAirline().getId() == member.getAirline().getId();
				validLeg = !leg.isDraftMode() && isMyAirline && (isFuture || isSameAsAssigned);
			}
			status = status && validLeg;

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
		int id;

		id = super.getRequest().getData("id", int.class);
		assignment = this.repository.findFlightAssignmentById(id);

		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		int legId;
		Leg leg;
		FlightCrewMember member;

		legId = super.getRequest().getData("flightLeg", int.class);
		leg = this.repository.findLegById(legId);
		int memberId = super.getRequest().getData("flightCrewMember", int.class);
		member = this.repository.findCrewMemberById(memberId);

		super.bindObject(assignment, "duty", "status", "remarks");
		assignment.setLastUpdate(MomentHelper.getCurrentMoment());
		assignment.setFlightLeg(leg);
		assignment.setFlightCrewMember(member);
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

		boolean legWithCopilot = assignedDuties.stream().filter(assignment -> assignment.getId() != flightAssignment.getId()).anyMatch(assignment -> assignment.getDuty().equals(FlightDuty.CO_PILOT));
		boolean legWithPilot = assignedDuties.stream().filter(assignment -> assignment.getId() != flightAssignment.getId()).anyMatch(assignment -> assignment.getDuty().equals(FlightDuty.PILOT));

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
		String employeeCode;
		FlightCrewMember member;
		Collection<FlightCrewMember> members;
		SelectChoices selectedMembers;

		member = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		legs = this.repository.findPublishedFutureOwnedLegs(MomentHelper.getCurrentMoment(), member.getAirline());
		members = this.repository.findCrewMembersByAirline(member.getAirline());
		Leg currentLeg = assignment.getFlightLeg();
		if (currentLeg != null && !legs.contains(currentLeg))
			legs.add(currentLeg);
		statuses = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());
		duties = SelectChoices.from(FlightDuty.class, assignment.getDuty());
		selectedLegs = SelectChoices.from(legs, "flightNumberDigits", assignment.getFlightLeg());
		employeeCode = assignment.getFlightCrewMember() != null ? assignment.getFlightCrewMember().getEmployeeCode() : null;
		selectedMembers = SelectChoices.from(members, "employeeCode", assignment.getFlightCrewMember());

		dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks", "draftMode");
		dataset.put("employeeCode", employeeCode);
		dataset.put("statuses", statuses);
		dataset.put("duties", duties);
		dataset.put("flightLeg", selectedLegs.getSelected().getKey());
		dataset.put("legs", selectedLegs);
		dataset.put("crewMembers", selectedMembers);

		super.getResponse().addData(dataset);
	}
}
