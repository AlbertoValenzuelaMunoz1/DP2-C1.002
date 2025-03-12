
package acme.entities.student1.flight;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.student1.manager.Manager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				tag;

	@Mandatory
	@Automapped
	private Boolean				transfer;

	@Mandatory
	@ValidNumber(fraction = 2)
	private Double				cost;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				description;

	@Mandatory
	@Valid
	@OneToOne(optional = false)
	private Manager				manager;

	@Mandatory
	@ValidMoment
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date				scheduledDeparture;

	@Mandatory
	@ValidMoment
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date				scheduledArrival;

	@Mandatory
	@ValidString
	@Automapped
	private String				originCity;

	@Mandatory
	@ValidString
	@Automapped
	private String				destinationCity;

	@Mandatory
	@ValidNumber
	@Automapped
	private Integer				numberOfLayovers;

}
