
package acme.entities.student5.Task;

import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.entities.student5.maintenanceRecord.MaintenanceRecord;

public class TaskRecord extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@ManyToOne(optional = false)
	@Mandatory
	@Valid
	private MaintenanceRecord	maintanenceRecord;

	@ManyToOne(optional = false)
	@Mandatory
	@Valid
	private Task				task;

}
