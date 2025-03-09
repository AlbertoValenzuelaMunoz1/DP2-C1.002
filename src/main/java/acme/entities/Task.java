
package acme.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.datatypes.enums.TaskType;
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
	@ValidString(max = 255)
	@Automapped
	private String				description;

	@Mandatory
	@ValidNumber(min = 0, max = 10, fraction = 0)
	@Automapped
	private Integer				priority;

	@Mandatory
	@ValidNumber(min = 0, fraction = 0)
	@Automapped
	private Integer				estimatedDuration;

	@ManyToOne
	@JoinColumn(name = "technician_id")
	private Technician			technician;

	@ManyToOne
	@JoinColumn(name = "maintenance_record_id")
	private MaintenanceRecord	maintenanceResources;

}
