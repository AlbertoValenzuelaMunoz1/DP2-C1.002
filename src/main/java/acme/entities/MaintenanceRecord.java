
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.FutureOrPresent;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.datatypes.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MaintenanceRecord extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	// @ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				moment;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private Status				status;

	@Mandatory
	@FutureOrPresent
	@Temporal(TemporalType.TIMESTAMP)
	private Date				nextInspectionDueDate;

	@Mandatory
	@ValidMoney(min = 0)
	@Automapped
	private Money				estimateCost;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				notes;

	// Un tecnico puede tener varios registros de mantenimiento

	@ManyToOne
	@JoinColumn(name = "technician_id")
	private Technician			technician;
}
