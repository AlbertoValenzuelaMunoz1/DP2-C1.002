
package acme.features.technician.involvedIn;

import java.util.Collection;

import acme.entities.student5.Task.TaskRecord;

public interface TechnicianInvolvedInRepository {

	void deleteAll(Collection<TaskRecord> involvedEntities);

}
