
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.student5.Task.Task;
import acme.entities.student5.Task.TaskRecord;

public interface TechnicianTaskRepository extends AbstractRepository {

	@Query("Select t from Technician t where t.id=:id")
	Collection<Task> findTasksByTechnicianId(int technicianId);

	@Query("SELECT t from Task t WHERE t.id=:id")
	Task findTaskById(int taskId);

	@Query("Select tr from TaskRecord tr where tr.task.id = : taskId")
	Collection<TaskRecord> findTaskRecordByTaskId(int id);

}
