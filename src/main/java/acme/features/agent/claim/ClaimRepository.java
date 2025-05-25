
package acme.features.agent.claim;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.student1.leg.Leg;
import acme.entities.student4.claim.Claim;
import acme.entities.student4.tranckingLog.TrackingLog;
import acme.realms.AssistanceAgent;

@Repository
public interface ClaimRepository extends AbstractRepository {

	@Query("SELECT c FROM Claim c")
	List<Claim> findAllClaims();

	@Query("SELECT c FROM Claim c WHERE c.id = :id")
	Claim findClaimById(@Param("id") int id);

	@Query("select a from AssistanceAgent a where a.id = :id")
	AssistanceAgent findAgentById(@Param("id") int id);

	@Query("SELECT c FROM Claim c WHERE c.assistanceAgent.id = :agentId")
	List<Claim> findAllClaimsByAssistanceAgentId(@Param("agentId") int agentId);

	@Query("select l from Leg l where l.id = :legId")
	Leg findLegById(int legId);

	@Query("select l from Leg l")
	Collection<Leg> findAllLegs();

	@Query("select l from Leg l where l.scheduledArrival < :currentMoment and l.draftMode is false")
	Collection<Leg> findAllPublishedCompletedLegs(Date currentMoment);

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = ?1 ORDER BY t.resolutionPercentage ASC")
	List<TrackingLog> getTrackingLogsByResolutionOrder(Integer claimId);

	@Query("select t from TrackingLog t where t.claim.id = :claimId")
	List<TrackingLog> findAllLogsFromClaim(@Param("claimId") Integer claimId);

}
