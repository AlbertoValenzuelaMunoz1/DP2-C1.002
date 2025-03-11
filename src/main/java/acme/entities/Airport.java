
package acme.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.datatypes.OperationalScope;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Airport extends AbstractEntity {

	//Serialisation ------------------------------

	private static final long	serialVersionUID	= 1L;

	//Attributes --------------------------------

	@Mandatory
	@NotBlank
	@Column(unique = true)
	@ValidString(min = 1, max = 50)
	private String				name;

	@Mandatory
	@NotBlank
	@Column(unique = true, length = 3)
	@ValidString(pattern = "^[A-Z]{3}$", message = "IATA code must be three uppercase letters")
	private String				iataCode;

	@Mandatory
	@NotNull
	@Enumerated(EnumType.STRING)
	@Automapped
	@Valid
	private OperationalScope	operationalScope;

	@Mandatory
	@NotBlank
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				city;

	@Mandatory
	@NotBlank
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				country;

	@Optional
	@ValidUrl
	@Automapped
	private String				website;

	@Optional
	@ValidEmail
	@Automapped
	private String				email;

	@Optional
	@ValidString(max = 50)
	@Automapped
	private String				address;

	@Optional
	@ValidString(pattern = "^\\+?\\d{6,15}$", message = "Invalid phone number format")
	@Automapped
	private String				contactPhoneNumber;

}
