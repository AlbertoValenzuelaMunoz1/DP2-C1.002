
package acme.features.authenticated.customer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.entities.student2.customer.Customer;

@Repository
public interface AuthenticatedCustomerRepository extends AbstractRepository {

	@Query("select ua from UserAccount ua where ua.id = :id")
	UserAccount findUserAccountById(int id);
	@Query("select c from Customer c where c.userAccount.id=:id")
	Customer findCustomerByUserAccountId(int id);
	@Query("select c from Customer c where c.identifier=:identifier")
	Customer findCustomerByIdentifier(String identifier);
	@Query("select c from Customer c where c.identifier=:identifier and not c.id=:excludeId")
	Customer findCustomerByIdentifier(String identifier, int excludeId);

}
