
package acme.features.administrator.passenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.student2.booking.Booking;
import acme.entities.student2.passenger.Passenger;

@Repository
public interface AdministratorPassengerRepository extends AbstractRepository {

	@Query("select r.passenger from BookingRecord r where r.booking.id=:id")
	public Collection<Passenger> findPassengersByBookingId(int id);
	@Query("select p from Passenger p where p.id=:id")
	public Passenger findPassengerById(int id);
	@Query("select b from Booking b where b.id=:id")
	public Booking findBookingById(int id);

}
