
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import acme.client.components.basis.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Claim extends AbstractEntity {

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	private Date		registrationMoment;

	@NotBlank
	@Email
	private String		passengerEmail;

	@NotBlank
	@Size(max = 255)
	private String		description;

	@NotNull
	@Enumerated(EnumType.STRING)
	private ClaimType	type;

	@NotNull
	private boolean		accepted;


	public enum ClaimType {
		FLIGHT_ISSUES, LUGGAGE_ISSUES, SECURITY_INCIDENT, OTHER_ISSUES
	}
}
