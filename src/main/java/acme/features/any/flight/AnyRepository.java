
package acme.features.any.flight;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.student1.flight.Flight;

@Repository
public interface AnyRepository extends AbstractRepository {

	@Query("SELECT f FROM Flight f WHERE f.draftMode = false")
	Collection<Flight> findPublishFlights();

	@Query("SELECT f from Flight f WHERE f.id = :id")
	Optional<Flight> findFlightById(int id);

}
