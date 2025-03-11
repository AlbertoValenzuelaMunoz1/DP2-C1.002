
package acme.entities.group.service;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidScore;
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
	@ValidString(max = 50)
	@Automapped
	private String				imageLink;

	@Mandatory
	@PositiveOrZero
	@ValidNumber(integer = 3, fraction = 2)
	@Automapped
	private Double				averageDwellTime;

	@Optional
	@ValidScore
	@ValidString(pattern = "^[A-Z]{4}-[0-9]{2}$", message = "El código promocional no es válido. Debe ser de 4 letras y 2 dígitos.")
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
