
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.student1.flight.Flight;
import acme.entities.student2.booking.Booking;
import acme.entities.student2.customer.Customer;

@Repository
public interface CustomerBookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.customer.id=:id")
	public Collection<Booking> findBookingsByCustomerId(int id);
	@Query("select b from Booking b where b.id=:id")
	public Booking findBookingById(int id);
	@Query("select c from Customer c where c.id=:id")
	public Customer findCustomerById(int id);
	@Query("select f from Flight f")
	public Collection<Flight> findAllFlights();
	@Query("select b from Booking b where b.locatorCode=:locatorCode")
	public Booking findByLocatorCode(String locatorCode);
}
