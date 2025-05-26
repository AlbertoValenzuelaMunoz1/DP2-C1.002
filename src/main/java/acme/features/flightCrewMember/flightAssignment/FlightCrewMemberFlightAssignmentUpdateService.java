
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
public class FlightCrewMemberFlightAssignmentUpdateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

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

		if (super.getRequest().getMethod().equals("POST")) {
			int legId = super.getRequest().getData("flightLeg", int.class);
			Leg leg = this.repository.findLegById(legId);

			validLeg = legId == 0 || leg != null;
			if (validLeg && leg != null) {
				boolean isSameAsAssigned = assignment.getFlightLeg() != null && leg.getId() == assignment.getFlightLeg().getId();
				boolean isFuture = MomentHelper.isBefore(MomentHelper.getCurrentMoment(), leg.getScheduledArrival());
				boolean isMyAirline = leg.getAircraft().getAirline().getId() == member.getAirline().getId();
				validLeg = !leg.isDraftMode() && isMyAirline && (isFuture || isSameAsAssigned);
			}
			status = status && validLeg;
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
		String employeeCode;
		FlightCrewMember member;

		member = assignment.getFlightCrewMember();

		Collection<FlightCrewMember> crewMembers = this.repository.findCrewMembersByAirline(member.getAirline());
		SelectChoices selectedCrew = SelectChoices.from(crewMembers, "employeeCode", assignment.getFlightCrewMember());

		legs = this.repository.findPublishedFutureOwnedLegs(MomentHelper.getCurrentMoment(), member.getAirline());
		Leg currentLeg = assignment.getFlightLeg();
		if (currentLeg != null && !legs.contains(currentLeg))
			legs.add(currentLeg);
		statuses = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());
		duties = SelectChoices.from(FlightDuty.class, assignment.getDuty());
		selectedLegs = SelectChoices.from(legs, "flightNumberDigits", assignment.getFlightLeg());
		employeeCode = assignment.getFlightCrewMember().getEmployeeCode();

		dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks", "draftMode");
		dataset.put("employeeCode", employeeCode);
		dataset.put("statuses", statuses);
		dataset.put("duties", duties);
		dataset.put("flightLeg", selectedLegs.getSelected().getKey());
		dataset.put("legs", selectedLegs);
		dataset.put("flightCrewMember", selectedCrew.getSelected().getKey());
		dataset.put("crewMembers", selectedCrew);

		super.getResponse().addData(dataset);
	}
}
