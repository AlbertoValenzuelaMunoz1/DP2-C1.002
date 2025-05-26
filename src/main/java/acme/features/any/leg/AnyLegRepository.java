
package acme.features.any.leg;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.group.aircraft.Aircraft;
import acme.entities.group.airport.Airport;
import acme.entities.student1.flight.Flight;
import acme.entities.student1.leg.Leg;

@Repository
public interface AnyLegRepository extends AbstractRepository {

	@Query("SELECT l FROM Leg l WHERE l.flight.id = :id")
	Collection<Leg> findLegById(int id);

	@Query("Select air from Aircraft air")
	Collection<Aircraft> findAllAircrafts();

	@Query("Select a from Airport a")
	Collection<Airport> findAllAirports();

	@Query("select f from Flight f where f.manager.id = :id")
	Collection<Flight> findFlightsByManagerId(int id);
}
