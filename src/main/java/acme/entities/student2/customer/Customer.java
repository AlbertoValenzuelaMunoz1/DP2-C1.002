
package acme.entities.student2.customer;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.Validators.ValidPhoneNumber;
import acme.client.components.basis.AbstractRole;
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
public class Customer extends AbstractRole {

	private static final long	serialVersionUID	= 1L;
	@Mandatory
	@Column(unique = true)
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$")
	private String				identifier;

	@Mandatory
	@Automapped
	@ValidPhoneNumber
	private String				phoneNumber;

	@Mandatory
	@Automapped
	@ValidString(max = 255, min = 1)
	private String				physicalAddres;

	@Mandatory
	@Automapped
	@ValidString(max = 50, min = 1)
	private String				city;

	@Mandatory
	@Automapped
	@ValidString(max = 50, min = 1)
	private String				country;

	@Optional
	@Automapped
	@ValidNumber(min = 0, max = 500000)
	private Integer				earnedPoints;

}
