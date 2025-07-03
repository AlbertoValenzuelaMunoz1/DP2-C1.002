
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.group.airline.Airline;
import acme.entities.student1.leg.Leg;
import acme.entities.student3.activityLog.ActivityLog;
import acme.entities.student3.flightAssignment.FlightAssignment;
import acme.entities.student3.flightCrewMember.FlightCrewMember;

@Repository
public interface FlightCrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("select fa from FlightAssignment fa where fa.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id = :memberId and fa.flightLeg.scheduledArrival < :now")
	Collection<FlightAssignment> findMyCompletedAssignments(Date now, Integer memberId);

	@Query("select fa from FlightAssignment fa where fa.draftMode = false and fa.flightLeg.scheduledArrival < :now")
	Collection<FlightAssignment> findCompletedPublishedAssignments(Date now);

	@Query("select fa from FlightAssignment fa where fa.draftMode = false and fa.flightLeg.scheduledArrival > :now")
	Collection<FlightAssignment> findPlannedPublishedAssignments(Date now);

	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id = :memberId and fa.flightLeg.scheduledArrival > :now")
	Collection<FlightAssignment> findMyPlannedAssignments(Date now, Integer memberId);

	@Query("select l from Leg l where l.draftMode = false and l.scheduledArrival > :now and l.aircraft.airline = :airline")
	Collection<Leg> findPublishedFutureOwnedLegs(Date now, Airline airline);

	@Query("select l from Leg l where l.id = :legId")
	Leg findLegById(int legId);

	@Query("select l from ActivityLog l where l.flightAssignment.id = :id")
	Collection<ActivityLog> findActivityLogsByAssignmentId(int id);

	@Query("select distinct fa.flightLeg from FlightAssignment fa where fa.flightCrewMember.id = :memberId and not fa.id = :flightAssignmentId")
	Collection<Leg> findLegsByFlightCrewMemberId(int memberId, int flightAssignmentId);

	@Query("select fa from FlightAssignment fa where fa.flightLeg.id = :legId")
	Collection<FlightAssignment> findFlightAssignmentByLegId(int legId);

	@Query("select fcm from FlightCrewMember fcm where fcm.airline = :airline")
	Collection<FlightCrewMember> findCrewMembersByAirline(Airline airline);

	@Query("select fcm from FlightCrewMember fcm where fcm.id = :id")
	FlightCrewMember findFlightCrewMemberById(int id);

	@Query("select fcm from FlightCrewMember fcm where fcm.id = :id")
	FlightCrewMember findCrewMemberById(int id);

}
