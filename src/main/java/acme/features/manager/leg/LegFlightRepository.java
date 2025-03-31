
package acme.features.manager.leg;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.group.aircraft.Aircraft;
import acme.entities.group.airport.Airport;
import acme.entities.student1.flight.Flight;
import acme.entities.student1.leg.Leg;

@Repository
public interface LegFlightRepository extends AbstractRepository {

	@Query("select l from Leg l where l.id = :legId")
	Optional<Leg> findLegById(int legId);

	@Query("select f from Flight f where f.id = :flightId")
	Optional<Flight> findFlightById(int flightId);

	@Query("select l from Leg l where l.flight.id = :flightId")
	Collection<Leg> findLegByFlightId(int flightId);

	@Query("select l.flight from Leg l where l.id = :legId")
	Optional<Flight> findFlightByLegId(int legId);

	@Query("Select air from Aircraft air")
	Collection<Aircraft> findAllAircrafts();

	@Query("Select a from Airport a")
	Collection<Airport> findAllAirports();

	@Query("select f from Flight f where f.manager.id = :id")
	Collection<Flight> findFlightsByManagerId(int id);
}
