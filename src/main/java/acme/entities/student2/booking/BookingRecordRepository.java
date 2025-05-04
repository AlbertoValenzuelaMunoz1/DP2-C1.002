
package acme.entities.student2.booking;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.student2.customer.Customer;
import acme.entities.student2.passenger.Passenger;

@Repository
public interface BookingRecordRepository extends AbstractRepository {

	@Query("select r.passenger from BookingRecord r where r.booking=:b")
	public List<Passenger> findPassengersBooking(Booking b);
	@Query("select count(r) from BookingRecord r where r.booking=:b")
	public int findCountPassengersBooking(Booking b);
	@Query("select r.passenger from BookingRecord r where r.booking.customer=:customer")
	public List<Passenger> findPassengersCustomerBookings(Customer c);

}
