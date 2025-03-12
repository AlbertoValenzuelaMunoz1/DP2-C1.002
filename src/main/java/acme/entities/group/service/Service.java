
package acme.entities.group.service;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.Validators.ValidPromotionCode;
import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.entities.group.airport.Airport;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Service extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				name;

	@Mandatory
	@ValidUrl
	@Automapped
	private String				imageLink;

	@Mandatory
	@ValidNumber(min = 0, integer = 3, fraction = 2)
	@Automapped
	private Double				averageDwellTime;

	@Optional
	@ValidPromotionCode
	@Automapped
	private String				promotionCode;

	@Optional
	@ValidMoney(min = 0)
	@Automapped
	private Money				discountAmount;

	@Mandatory
	@Valid
	@ManyToOne
	private Airport				airport;

}
