
package acme.features.administrator.airport;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.group.airport.Airport;

@Repository
public interface AirportRepository extends AbstractRepository {

	@Query("SELECT a FROM Airport a")
	List<Airport> findAllAirports();

	@Query("SELECT a FROM Airport a WHERE a.id = :id")
	Airport findOneById(@Param("id") int id);

}
