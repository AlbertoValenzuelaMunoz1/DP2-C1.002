
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.student3.flightAssignment.FlightAssignment;
import acme.entities.student3.flightCrewMember.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentListLegsCompletedService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<FlightAssignment> assignments;
		int id;
		Date now;

		id = super.getRequest().getPrincipal().getActiveRealm().getId();
		now = MomentHelper.getCurrentMoment();

		assignments = this.repository.findCompletedFlightAssignmentsByMemberId(now, id);

		super.getBuffer().addData(assignments);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;

		dataset = super.unbindObject(assignment, "lastUpdate", "status", "Duty");
		super.addPayload(dataset, assignment, "remarks");

		super.getResponse().addData(dataset);
	}
}
