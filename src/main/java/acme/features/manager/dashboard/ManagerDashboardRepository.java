
package acme.features.manager.dashboard;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.student1.flight.Flight;
import acme.entities.student1.leg.Leg;
import acme.realms.Manager;

@Repository
public interface ManagerDashboardRepository extends AbstractRepository {

	@Query("SELECT m.id FROM Manager m ORDER BY m.yearsOfExperience DESC")
	public List<Integer> managersRanking();

	@Query("SELECT m FROM Manager m WHERE m.id = :id")
	public Manager findManagerById(int id);

	@Query("SELECT f FROM Flight f WHERE f.manager.id = :id")
	public List<Flight> flightsByManager(int id);

	@Query("SELECT l FROM Leg l WHERE l.flight.manager.id = :id and l.status = acme.entities.student1.leg.LegStatus.DELAYED")
	public List<Leg> delayedLegs(int id);

	@Query("SELECT l FROM Leg l WHERE l.flight.manager.id = :id and l.status = acme.entities.student1.leg.LegStatus.ON_TIME")
	public List<Leg> onTimeLegs(int id);

	@Query("SELECT l FROM Leg l WHERE l.status = acme.entities.student1.leg.LegStatus.LANDED and l.flight.manager.id=:id")
	public List<Leg> landedLegs(int id);

	@Query("SELECT l FROM Leg l WHERE l.flight.manager.id = :id and l.status = acme.entities.student1.leg.LegStatus.CANCELLED")
	public List<Leg> canceledLegs(int id);

	@Query("SELECT AVG(f.cost.amount) FROM Flight f WHERE f.manager.id = :id")
	public Double averageFlightsCost(int id);

	@Query("SELECT MAX(f.cost.amount) FROM Flight f WHERE f.manager.id = :id")
	public Double maxFlightsCost(int id);

	@Query("SELECT MIN(f.cost.amount) FROM Flight f WHERE f.manager.id = :id")
	public Double minFlightsCost(int id);

	@Query("SELECT STDDEV(f.cost.amount) FROM Flight f WHERE f.manager.id = :id")
	public Double desvFlightsCost(int id);
}
