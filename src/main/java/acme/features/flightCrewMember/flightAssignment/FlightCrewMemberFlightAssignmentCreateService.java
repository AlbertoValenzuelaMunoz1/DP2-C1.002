
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.AssignmentStatus;
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

		if (super.getRequest().getMethod().equals("POST")) {
			int legId = super.getRequest().getData("flightLeg", int.class);
			Leg leg = this.repository.findLegById(legId);

			validLeg = legId == 0 || leg != null;
			if (validLeg && leg != null) {
				member = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
				boolean isFuture = MomentHelper.isBefore(MomentHelper.getCurrentMoment(), leg.getScheduledArrival());
				boolean isMyAirline = leg.getAircraft().getAirline().getId() == member.getAirline().getId();
				validLeg = !leg.isDraftMode() && isFuture && isMyAirline;
				status = validLeg;
			}
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
		;
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
