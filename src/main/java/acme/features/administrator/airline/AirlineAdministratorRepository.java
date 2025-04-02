
package acme.features.administrator.airline;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.group.airline.Airline;

@Repository
public interface AirlineAdministratorRepository extends AbstractRepository {

	@Query("select a from Airline a")
	public Collection<Airline> findAllAirlines();
	@Query("select a from Airline a where a.id=:id")
	public Airline findAirlineById(int id);
	@Query("select a from Airline a where a.iataCode=:iataCode and not a.id=:excludeId")
	public Airline findAirlineByIataCode(String iataCode, int excludeId);
	@Query("select a from Airline a where a.iataCode=:iataCode")
	public Airline findAirlineByIataCode(String iataCode);
}
