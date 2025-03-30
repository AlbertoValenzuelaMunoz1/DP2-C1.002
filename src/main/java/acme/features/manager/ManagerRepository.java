
package acme.features.manager;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.student1.flight.Flight;
import acme.entities.student1.leg.Leg;
import acme.entities.student1.manager.Manager;

@Repository
public interface ManagerRepository extends AbstractRepository {

	@Query("select m from Manager m where m.identifier = :identifier")
	Manager findManagerByIdentifier(String identifier);

	@Query("select f from Flight f where f.id = :id")
	Flight findFlightById(int id);

	@Query("select f from Flight f where f.manager.id = :Id")
	Collection<Manager> findAllFligths();

	@Query("select f from Flight f where f.manager.id = :id")
	Collection<Flight> findFlightsByManagerId(int id);

	@Query("select count(l) from Legs l where l.flight.id = :id")
	Integer findNumberLegsByFlightId(int id);

	@Query("select l from Legs l where l.flight.id = :id")
	Collection<Leg> findAllLegsByFlightId(int id);

}
