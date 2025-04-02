
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.student2.booking.Booking;
import acme.entities.student2.booking.BookingRecord;
import acme.entities.student2.customer.Customer;
import acme.entities.student2.passenger.Passenger;

@Repository
public interface CustomerPassengerRepository extends AbstractRepository {

	@Query("select p from Passenger p where p.customer.id=:customerId")
	public Collection<Passenger> findCustomerPassengers(int customerId);

	@Query("select p from Passenger p where p.id=:id")
	public Passenger findPassengerById(int id);
	@Query("select c from Customer c where c.id=:id")
	public Customer findCustomerById(int id);
	@Query("select r.passenger from BookingRecord r where r.booking.id=:id")
	public Collection<Passenger> findPassengersByBookingId(int id);
	@Query("select b from Booking b where b.id=:id")
	public Booking findBookingById(int id);
	@Query("select r from BookingRecord r where r.passenger.id=:id")
	public Collection<BookingRecord> findBookingRecordByPassengerId(int id);
}
