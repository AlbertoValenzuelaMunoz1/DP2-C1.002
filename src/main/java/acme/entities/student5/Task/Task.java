
package acme.entities.student5.Task;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.datatypes.TaskType;
import acme.entities.student5.maintenanceRecord.MaintenanceRecord;
import acme.entities.student5.technician.Technician;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Task extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private TaskType			taskType;

	@Mandatory
	@ValidString(min = 1, max = 255)
	@Automapped
	private String				description;

	@Mandatory
	@ValidNumber(min = 0, max = 10, fraction = 0)
	@Automapped
	private Integer				priority;

	@Mandatory
	@ValidNumber(min = 0, max = 1000, fraction = 0)
	@Automapped
	private Integer				estimatedDuration;

	@ManyToOne
	private Technician			technician;

	@ManyToOne
	private MaintenanceRecord	maintenanceRecord;

}
