
package acme.features.agent.trackingLog;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.student4.claim.Claim;
import acme.entities.student4.tranckingLog.TrackingLog;

@Repository
public interface TrackingLogRepository extends AbstractRepository {

	@Query("select t from TrackingLog t")
	List<TrackingLog> findAllLogs();

	@Query("SELECT l FROM TrackingLog l WHERE l.id = :id")
	TrackingLog findLogById(@Param("id") int id);

	@Query("select t from TrackingLog t where t.claim.id = :claimId")
	List<TrackingLog> findAllLogsFromClaim(@Param("claimId") Integer claimId);

	@Query("""
		SELECT tl
		FROM TrackingLog tl
		WHERE tl.claim.id = :claimId
		ORDER BY tl.resolutionPercentage DESC
		""")
	List<TrackingLog> findTopPercentage(int claimId);

	@Query("SELECT c FROM Claim c WHERE c.id = :id")
	Claim findClaimById(@Param("id") int id);

	//	@Query("select t from TrackingLog t where t.claim.id = :claimId order by t.resolutionPercentage desc")
	//	TrackingLog findFirstByClaimIdOrderByResolutionPercentageDesc(@Param("claimId") Integer claimId);

	TrackingLog findFirstByClaimIdOrderByResolutionPercentageDesc(Integer claimId);

	@Query("select count(t) from TrackingLog t where t.claim.id = :claimId and t.resolutionPercentage = 100.00")
	int countFullyResolvedLogs(@Param("claimId") int claimId);

	@Query("select max(t.resolutionPercentage) from TrackingLog t where t.claim.id = :claimId")
	Double findMaxResolutionPercentageByClaimId(@Param("claimId") int claimId);

}
