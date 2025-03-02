
package acme.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;
	@Mandatory
	@Column(unique = true)
	@ValidString(pattern = "^[A-Z]{2-3}\\d{6}$")
	private String				identifier;
	@Mandatory
	@Automapped
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	private String				phoneNumber;
	@Mandatory
	@Automapped
	@ValidString(max = 255)
	private String				physicalAddres;
	@Mandatory
	@Automapped
	@ValidString(max = 50)
	private String				city;
	@Mandatory
	@Automapped
	@ValidString(max = 50)
	private String				country;
	@Mandatory
	@Automapped
	@ValidNumber(min = 0, max = 500000)
	private Integer				earnedPoints;

}
