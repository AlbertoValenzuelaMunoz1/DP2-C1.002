
package acme.entities.student2.passenger;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.entities.student2.customer.Customer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Passenger extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;
	@Mandatory
	@Automapped
	@ValidString(max = 255, min = 1)
	private String				fullName;

	@Mandatory
	@Automapped
	@ValidEmail
	private String				email;

	@Mandatory
	@Automapped
	@ValidString(pattern = "^[A-Z0-9]{6,9}$")
	private String				passportNumber;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(past = true)
	private Date				dateOfBirth;

	@Optional
	@Automapped
	@ValidString(max = 50, min = 0)
	private String				specialNeeds;
	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Customer			customer;
	@Mandatory
	@Automapped
	private boolean				published;
}
