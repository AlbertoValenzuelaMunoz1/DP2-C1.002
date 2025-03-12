
package acme.entities.student3.flightCrewMember;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.Validators.ValidPhoneNumber;
import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.datatypes.AvailabilityStatus;
import acme.entities.group.airline.Airline;
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
	@ValidPhoneNumber
	private String				phoneNumber;

	@Automapped
	@Mandatory
	@ValidString(max = 255)
	private String				languageSkills;

	@Automapped
	@Mandatory
	@Valid
	private AvailabilityStatus	availabilityStatus;

	@Automapped
	@Mandatory
	@ValidNumber(min = 0)
	private double				salary;

	@Automapped
	@Optional
	@ValidNumber(min = 0)
	private Integer				yearsOfExperience;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Automapped
	@ManyToOne(optional = false)
	private Airline				airline;

}
