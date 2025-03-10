
package acme.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Moment;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Manager extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2-3}\\\\d{6}$")
	@Automapped
	private String				identifierNumber;

	@Mandatory
	@ValidNumber(min = 0)
	@Automapped
	private Integer				experienceYears;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(value = TemporalType.TIMESTAMP)
	private Moment				birthDate;

	@Optional
	@ValidUrl
	@Automapped
	private String				pictureLink;

	@ManyToOne(optional = false)
	@Mandatory
	@Automapped
	private Airline				airline;
}
