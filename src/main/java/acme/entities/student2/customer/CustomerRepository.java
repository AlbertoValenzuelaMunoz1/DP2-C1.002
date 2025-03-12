
package acme.entities.student2.customer;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface CustomerRepository extends AbstractRepository {

	@Query("select sum(b.price.amount) from Booking b where b.customer=:customer")
	public Double findMoneySpent(final Customer customer);
	//	@Query("select b from Booking b join b.flight.legs where b.customer=:customer order by ")
	//	public List<Flight> findLastFlights(final Customer customer, Pageable pageable);
	@Query("select count(b),b.travelClass from Booking b where b.customer=:customer group by b.travelClass")
	public List<Object[]> findBookingsPerTravelClass(final Customer customer);
	@Query("select count(b),avg(b.price.amount),min(b.price.amount),max(b.price.amount),stddev(b.price.amount) from Booking b where b.customer=:customer and year(b.purchaseMoment)>=:minimumYear")
	public Object[] findCostStatistics(final Customer customer, Integer minimumYear);
	@Query("select avg(select count(r) from BookingRecord r where r=b) from Booking b where b.customer=:customer")
	public Double findAverageBookingPassenger(final Customer customer);
	@Query("select count(select count(r) from BookingRecord r where r=b) from Booking b where b.customer=:customer")
	public Long findCountBookingPassenger(final Customer customer);
	@Query("select max(select count(r) from BookingRecord r where r=b) from Booking b where b.customer=:customer")
	public Integer findMaxBookingPassenger(final Customer customer);
	@Query("select min(select count(r) from BookingRecord r where r=b) from Booking b where b.customer=:customer")
	public Integer findMinBookingPassenger(final Customer customer);
}
