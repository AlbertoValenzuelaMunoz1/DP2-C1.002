
package acme.entities.group.airline;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.Validators.ValidPhoneNumber;
import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.datatypes.AirlineType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Airline extends AbstractEntity {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	@Automapped
	@Mandatory
	@ValidString(min = 1, max = 50)
	private String				name;

	@Column(unique = true)
	@ValidString(min = 3, max = 3, pattern = "^[A-Z]{2}X$")
	@Mandatory
	private String				iataCode;

	@Automapped
	@Mandatory
	@ValidUrl
	private String				website;

	@Temporal(TemporalType.TIMESTAMP)
	@Mandatory
	@ValidMoment(past = true)
	private Date				foundationMoment;

	@Automapped
	@Mandatory
	@Valid
	private AirlineType			type;

	@Automapped
	@Optional
	@ValidEmail
	private String				email;

	@Automapped
	@Optional
	@ValidPhoneNumber
	private String				phoneNumber;
}
