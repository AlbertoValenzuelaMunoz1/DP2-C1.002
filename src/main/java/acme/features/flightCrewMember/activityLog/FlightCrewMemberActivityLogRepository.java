
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.student1.leg.Leg;
import acme.entities.student3.activityLog.ActivityLog;
import acme.entities.student3.flightAssignment.FlightAssignment;

@Repository
public interface FlightCrewMemberActivityLogRepository extends AbstractRepository {

	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id = :memberId")
	Collection<FlightAssignment> findFlightAssignmentsByMemberId(int memberId);

	@Query("select al from ActivityLog al where al.id = :logId")
	ActivityLog findActivityLogById(int logId);

	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id = :memberId or fa.draftMode = false")
	Collection<FlightAssignment> findFlightAssignmentsByMemberIdOrPublished(int memberId);

	@Query("select fa from FlightAssignment fa where fa.id = :assignmentId")
	FlightAssignment findFlightAssignmentById(Integer assignmentId);

	@Query("select lo from ActivityLog lo where lo.flightAssignment.id = :masterId")
	Collection<ActivityLog> findActivityLogsByMasterId(int masterId);

	@Query("select lo from ActivityLog lo where lo.flightAssignment.id = :masterId and lo.draftMode = false")
	Collection<ActivityLog> findPublishedActivityLogsByMasterId(int masterId);
}