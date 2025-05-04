
package acme.entities.student5.maintenanceRecord;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.datatypes.Status;
import acme.entities.group.aircraft.Aircraft;
import acme.entities.student5.technician.Technician;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MaintenanceRecord extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				moment;

	@Mandatory
	@Valid
	@Automapped
	private Status				status;

	@Mandatory
	@ValidString(pattern = "^[A-Za-z0-9]{6,8}$")
	@Automapped
	private String				nextInspectionDueDate;

	@Mandatory
	@ValidMoney(min = 0)
	@Automapped
	private Money				estimateCost;

	@Optional
	@ValidString(min = 1, max = 255)
	@Automapped
	private String				notes;

	// Un tecnico puede tener varios registros de mantenimiento

	@ManyToOne
	@Valid
	@Automapped
	private Technician			technician;

	@ManyToOne
	@Valid
	@Automapped
	private Aircraft			aircraft;
}
