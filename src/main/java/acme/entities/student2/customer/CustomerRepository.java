
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
	/*
	 * No es posible sacar la media, el máximo, el mínimo, etc en la consulta porque
	 * jpql no admite subconsultas dentro del from, en sql sí sería posible pero para
	 * seguir las pautas de la asignatura se realizará en jpql y solo se obtendrá el numero
	 * de pasageros por booking y los cálculos estadísticos se harán una vez obtenida
	 * la consulta
	 */
	@Query("select count(r.booking) from BookingRecord r where r.booking.customer=:customer group by r.booking")
	public List<Long> findNumberOfPassengersPerBooking(final Customer customer);
}
