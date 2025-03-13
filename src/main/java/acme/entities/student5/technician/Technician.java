
package acme.entities.student5.technician;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;

import acme.Validators.ValidPhoneNumber;
import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Technician extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$")
	@Column(unique = true)
	private String				licenseNumber;

	@Mandatory
	@ValidPhoneNumber
	@Automapped
	private String				phoneNumber;

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				specialization;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				annualHealthTest;

	@Mandatory
	@ValidNumber(min = 0, max = 1000)
	@Automapped
	private Integer				yearsOfExperience;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				certifications;

}
