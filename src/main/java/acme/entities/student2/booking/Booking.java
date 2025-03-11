
package acme.entities.student2.booking;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.datatypes.TravelClass;
import acme.entities.student1.flight.Flight;
import acme.entities.student2.customer.Customer;
import acme.entities.student2.passenger.Passenger;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Booking extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@ManyToOne
	@Mandatory
	@Valid
	private Customer			customer;

	@ManyToOne
	@Mandatory
	@Valid
	private Flight				flight;

	@OneToOne
	@Mandatory
	@Valid
	private Passenger			passenger;

	@Mandatory
	@Column(unique = true)
	@ValidString(pattern = "^[A-Z0-9]{6,8}$")
	private String				locatorCode;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(past = true)
	private Date				purchaseMoment;

	@Mandatory
	@Automapped
	@Valid
	private TravelClass			travelClass;

	@Mandatory
	@Automapped
	@ValidMoney(min = 0.)
	private Money				price;

	@Optional
	@Automapped
	@ValidString(min = 4, max = 4, pattern = "^[0-9]{4}$")
	private String				lastNibble;

}
