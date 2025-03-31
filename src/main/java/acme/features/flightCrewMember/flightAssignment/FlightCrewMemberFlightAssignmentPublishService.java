
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

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);
		memberId = super.getRequest().getData("member", int.class);
		member = this.repository.findFlightCrewMemberById(memberId);

		super.bindObject(assignment, "Duty", "status", "remarks");
		assignment.setFlightLeg(leg);
		assignment.setFlightCrewMember(member);
		assignment.setLastUpdate(MomentHelper.getCurrentMoment());
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		boolean status;

		status = assignment.isDraftMode();

		super.getResponse().setAuthorised(status);
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

		dataset = super.unbindObject(assignment, "Duty", "lastUpdate", "status", "remarks", "draftMode");
		dataset.put("confirmation", false);
		dataset.put("statuses", statuses);
		dataset.put("duties", duties);
		dataset.put("leg", selectedLegs.getSelected().getKey());
		dataset.put("legs", selectedLegs);
		dataset.put("member", selectedMembers.getSelected().getKey());
		dataset.put("members", selectedMembers);

		super.getResponse().addData(dataset);
	}
}
