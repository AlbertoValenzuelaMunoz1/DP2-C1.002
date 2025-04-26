
package acme.features.customer.dashboard;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.student1.flight.Flight;

@Repository
public interface CustomerDashboardRepository extends AbstractRepository {

	@Query("select sum(select count(r)*b.flight.cost.amount from BookingRecord r where r.booking=b) from Booking b where b.customer.id=:id and b.purchaseMoment>=:minimumDate and b.published=true")
	public Double findSumPriceByCustomerId(int id, Date minimumDate);
	@Query("select b.flight from Booking b where b.customer.id=:id and b.published=true order by b.purchaseMoment desc")
	public List<Flight> findLastFlightsByCustomerId(int id, Pageable pageable);
	@Query("select min(select count(r)*b.flight.cost.amount from BookingRecord r where r.booking=b) from Booking b where b.customer.id=:id and b.published=true and b.purchaseMoment>=:minimumDate")
	public Double findMinPriceByCustomerId(int id, Date minimumDate);
	@Query("select max(select count(r)*b.flight.cost.amount from BookingRecord r where r.booking=b) from Booking b where b.customer.id=:id and b.published=true and b.purchaseMoment>=:minimumDate")
	public Double findMaxPriceByCustomerId(int id, Date minimumDate);
	@Query("select avg(select count(r)*b.flight.cost.amount from BookingRecord r where r.booking=b) from Booking b where b.customer.id=:id and b.published=true and b.purchaseMoment>=:minimumDate")
	public Double findAveragePriceByCustomerId(int id, Date minimumDate);
	@Query("select count(b),b.travelClass from Booking b where b.customer.id=:id and b.published=true group by b.travelClass")
	public List<Object[]> findBookingsPerTravelClass(final int id);
	// Con la funcion stddev no permite hacerlo de igual manera que con avg así que hay que calcularlo en Java
	@Query("select count(r)*r.booking.flight.cost.amount from BookingRecord r where r.booking.purchaseMoment>=:minimumDate and r.booking.customer.id=:id and r.booking.published=true group by r.booking")
	public List<Double> findBookingCostsByCustomerId(int id, Date minimumDate);
	@Query("select max(select count(r) from BookingRecord r where r.booking=b) from Booking b where b.customer.id=:id and b.published=true")
	public Integer findMaxBookingPassenger(final int id);
	@Query("select min(select count(r) from BookingRecord r where r.booking=b) from Booking b where b.customer.id=:id and b.published=true")
	public Integer findMinBookingPassenger(final int id);
	@Query("select avg(select count(r) from BookingRecord r where r.booking=b) from Booking b where b.customer.id=:id and b.published=true")
	public Double findAvgBookingPassenger(final int id);
	@Query("select count(r.booking) from BookingRecord r where r.booking.published=true and r.booking.customer.id=:id group by r.booking.id")
	public List<Integer> findCountBookingPassenger(final int id);
}
