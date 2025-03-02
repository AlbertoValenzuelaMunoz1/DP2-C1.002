
package acme.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidValue;
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
	@ValidString(max = 50)
	private String				name;
	@Column(unique = true)
	@ValidString(min = 3, max = 3, pattern = "^[A-Z]{2}X$")
	@Mandatory
	private String				iataCode;
	@Automapped
	@Mandatory
	@ValidUrl
	private String				website;
	@Automapped
	@Mandatory
	@ValidMoment(past = true)
	private Date				foundationMoment;
	@Automapped
	@Mandatory
	@ValidValue(values = {
		"LUXURY", "STANDARD", "LOW-COST"
	})
	private String				type;
	@Automapped
	@Optional
	@ValidEmail
	private String				email;
	@Automapped
	@Optional
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	private String				phoneNumber;
}
