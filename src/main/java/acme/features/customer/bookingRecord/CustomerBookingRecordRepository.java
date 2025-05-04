
package acme.features.customer.bookingRecord;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.student2.booking.Booking;
import acme.entities.student2.booking.BookingRecord;
import acme.entities.student2.customer.Customer;
import acme.entities.student2.passenger.Passenger;

@Repository
public interface CustomerBookingRecordRepository extends AbstractRepository {

	@Query("select p from Passenger p where p.id=:id")
	public Passenger findPassengerById(int id);
	@Query("select c from Customer c where c.id=:id")
	public Customer findCustomerById(int id);
	@Query("select r from BookingRecord r where r.id=:id")
	public BookingRecord findBookingRecordById(int id);
	@Query("select p from Passenger p where p.customer.id=:customerId and not exists(select r from BookingRecord r where r.passenger=p and r.booking.id=:bookingId) ")
	public List<Passenger> findAvailablePassengersForBooking(int customerId, int bookingId);
	@Query("select b from Booking b where b.id=:id")
	public Booking findBookingById(int id);
	@Query("select r from BookingRecord r where r.booking.id=:id")
	public Collection<BookingRecord> findBookingRecordByBookingId(int id);
}
