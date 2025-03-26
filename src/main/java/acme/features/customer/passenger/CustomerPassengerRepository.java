
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.student2.customer.Customer;
import acme.entities.student2.passenger.Passenger;

@Repository
public interface CustomerPassengerRepository extends AbstractRepository {

	@Query("select p from Passenger p where p.customer.id=:customerId")
	public Collection<Passenger> findCustomerPassengers(Integer customerId);

	@Query("select p from Passenger p where p.id=:id")
	public Passenger findPassengerById(Integer id);
	@Query("select c from Customer c where c.id=:id")
	public Customer findCustomerById(Integer id);

}
