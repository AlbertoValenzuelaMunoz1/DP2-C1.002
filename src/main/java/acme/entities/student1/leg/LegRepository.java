
package acme.entities.student1.leg;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import acme.entities.student1.flight.Flight;

public interface LegRepository extends JpaRepository<Leg, Integer> {

	@Query("SELECT leg FROM Leg leg WHERE leg.flight = :flight ORDER BY leg.scheduledDeparture ASC")
	List<Leg> firstFlightLeg(@Param("flight") Flight flight);

	@Query("SELECT leg FROM Leg leg WHERE leg.flight = :flight ORDER BY leg.scheduledDeparture DESC")
	List<Leg> lastFlightLeg(@Param("flight") Flight flight);

	@Query("SELECT COUNT(leg) FROM Leg leg WHERE leg.flight =:flight")
	Long numberOfLegs(@Param("flight") Flight flight);

}
