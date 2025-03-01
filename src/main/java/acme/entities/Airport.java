
package acme.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import acme.client.components.basis.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Airport extends AbstractEntity {

	//Serialisation -------------------------------

	private static final long	serialVersionUID	= 1L;

	//Attributes ---------------------------------

	@NotBlank
	@Size(max = 50)
	private String				name;

	@NotBlank
	@Column(unique = true, length = 3)
	@Pattern(regexp = "^[A-Z]{3}$", message = "IATA code must be three uppercase letters")
	private String				iataCode;

	@Enumerated(EnumType.STRING)
	private OperationalScope	operationalScope;

	@NotBlank
	@Size(max = 50)
	private String				city;

	@NotBlank
	@Size(max = 50)
	private String				country;

	@Column(nullable = true)
	private String				website;

	@Email
	@Column(nullable = true)
	private String				email;

	@Pattern(regexp = "^\\+?\\d{6,15}$", message = "Invalid phone number format")
	@Column(nullable = true)
	private String				contactPhoneNumber;


	public enum OperationalScope {
		INTERNATIONAL, DOMESTIC, REGIONAL
	}
}
