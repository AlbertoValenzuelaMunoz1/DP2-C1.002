
package acme.features.manager.flight;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.student1.flight.Flight;
import acme.entities.student1.leg.Leg;
import acme.realms.Manager;

@Repository
public interface ManagerRepository extends AbstractRepository {

	@Query("select m from Manager m where m.identifier = :identifier")
	Optional<Manager> findManagerByIdentifier(String identifier);

	@Query("select f from Flight f where f.id = :id")
	Optional<Flight> findFlightById(int id);

	@Query("select f from Flight f where f.manager.id = :id")
	Collection<Flight> findFlightsByManagerId(int id);

	@Query("SELECT leg FROM Leg leg WHERE leg.flight.id = :id")
	Collection<Leg> findLegsByFlightId(int id);

	//	@Query("select f from Flight f where f.id = :flightId and f.manager.id = :id")
	//	Flight findFlightsByManagerIdAndFlightId(int managerId, int flightId);

	//	@Query("select count(l) from Legs l where l.flight.id = :id")
	//	Integer findNumberLegsByFlightId(int id);
	//
	//	@Query("select l from Legs l where l.flight.id = :id")
	//	Collection<Leg> findAllLegsByFlightId(int id);

}
