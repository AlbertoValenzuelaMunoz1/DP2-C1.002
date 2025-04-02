
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.group.aircraft.Aircraft;
import acme.entities.student5.Task.Task;
import acme.entities.student5.maintenanceRecord.MaintenanceRecord;

@Repository
public interface TechnicianMaintenanceRecordRepository extends AbstractRepository {

	@Query("select mr from MaintenanceRecord mr where mr.technician.id = :technicianId")
	Collection<MaintenanceRecord> findMaintenanceRecordsByTechnicianId(int technicianId);

	@Query("SELECT mr FROM MaintenanceRecord mr WHERE mr.id = :id")
	MaintenanceRecord findMaintenanceRecordById(int id);

	@Query("SELECT a FROM Aircraft a")
	Collection<Aircraft> findAvailableAircrafts();

	@Query("SELECT ii.task FROM InvolvedIn ii WHERE ii.maintenanceRecord.id = :id")
	Collection<Task> findTasksByMaintenanceRecordId(int id);

}
