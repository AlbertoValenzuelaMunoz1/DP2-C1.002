
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
	//	public List<String> findLastDestinations(final Customer customer, Pageable pageable);
	@Query("select count(b),b.travelClass from Booking b where b.customer=:customer group by b.travelClass")
	public List<Object[]> findBookingsPerTravelClass(final Customer customer);
	@Query("select count(b),avg(b.price.amount),min(b.price.amount),max(b.price.amount),stddev(b.price.amount) from Booking b where b.customer=:customer and year(b.purchaseMoment)>=:minimumYear")
	public Object[] findCostStatistics(final Customer customer, Integer minimumYear);
	@Query("select count(count(r.booking)),avg(count(r.booking)),max(count(r.booking)),min(count(r.booking)),stddev(count(r.booking)) from BookingRecord r where r.booking.customer=:customer group by r.booking")
	public Object[] findPassengerStatistics(final Customer customer);
}
