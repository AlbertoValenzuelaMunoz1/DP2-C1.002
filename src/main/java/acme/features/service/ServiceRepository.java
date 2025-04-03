
package acme.features.service;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.group.service.Service;

public interface ServiceRepository {

	@Query("SELECT s FROM Service s WHERE s.id=:id")
	Service findServiceById(int id);

	@Query("SELECT s FROM Service s")
	Collection<Service> findAllService();

}
