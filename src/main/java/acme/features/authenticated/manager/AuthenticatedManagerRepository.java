
package acme.features.authenticated.manager;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.entities.group.airline.Airline;
import acme.realms.Manager;

@Repository
public interface AuthenticatedManagerRepository extends AbstractRepository {

	@Query("select ua from UserAccount ua where ua.id = :id")
	UserAccount findUserAccountById(int id);

	@Query("select m from Manager m where m.userAccount.id = :id")
	Manager findManagerByUserAccountId(int id);

	@Query("select m from Manager m where m.identifier=:identifier")
	Manager findManagerByIdentifier(String identifier);

	@Query("select m from Manager m where m.identifier = :identifier and not m.id = :excludeId")
	Manager findManagerByIdentifier(String identifier, int excludeId);

	@Query("select air from Airline air")
	List<Airline> findAirlines();

}
