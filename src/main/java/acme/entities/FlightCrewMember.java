
package acme.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidString;
import acme.datatypes.AvailabilityStatus;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightCrewMember extends AbstractRole {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Column(unique = true)
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$")
	private String				employeeCode;

	@Automapped
	@Mandatory
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	private String				phoneNumber;

	@Automapped
	@Mandatory
	@ValidString(max = 255)
	private String				languageSkills;

	@Automapped
	@Mandatory
	@Enumerated(EnumType.STRING)
	private AvailabilityStatus	availabilityStatus;

	@Automapped
	@Mandatory
	@ValidString
	private String				airline;

	@Automapped
	@Mandatory
	@Min(0)
	private double				salary;

	@Automapped
	@Optional
	@Min(0)
	private Integer				yearsOfExperience;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
