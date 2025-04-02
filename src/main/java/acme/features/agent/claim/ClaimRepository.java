
package acme.features.agent.claim;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.student4.claim.Claim;

@Repository
public interface ClaimRepository extends AbstractRepository {

	@Query("SELECT c FROM Claim c")
	List<Claim> findAllClaims();

	@Query("SELECT c FROM Claim c WHERE c.id = :id")
	Claim findClaimById(@Param("id") int id);

	@Query("SELECT c FROM Claim c WHERE c.assistanceAgent.id = :agentId")
	List<Claim> findAllClaimsByAssistanceAgentId(@Param("agentId") int agentId);
}
