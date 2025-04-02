
package acme.features.technician;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.student5.maintenanceRecord.MaintenanceRecord;

@Repository
public interface TechnicianMaintenanceRecordRepository extends AbstractRepository {

	@Query("select m from MaintenanceRecord m where m.technician.id = :technicianId")
	Collection<MaintenanceRecord> findMaintenanceRecordsByTechnicianId(int technicianId);

}
