
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Review extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Automapped
	@ValidString(max = 50)
	private String				name;

	@Mandatory
	@Temporal(value = TemporalType.TIMESTAMP)
	@ValidMoment(past = true)
	private Date				moment;

	@Mandatory
	@Automapped
	@ValidString(max = 50)
	private String				subject;

	@Mandatory
	@Automapped
	@ValidString(max = 255)
	private String				text;

	@Optional
	@Automapped
	@ValidNumber(min = 0, max = 10, integer = 2, fraction = 2)
	private Double				score;

	@Optional
	@Automapped
	private Boolean				recommendation;

}
