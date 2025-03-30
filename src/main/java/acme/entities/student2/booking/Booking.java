
package acme.entities.student2.booking;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
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

	@Optional
	@Automapped
	@ValidString(min = 4, max = 4, pattern = "^[0-9]{4}$")
	private String				lastNibble;


	@Transient
	public List<Passenger> passengers() {
		BookingRecordRepository repository = SpringHelper.getBean(BookingRecordRepository.class);
		return repository.findPassengersBooking(this);
	}
	@Transient
	public Money price() {
		BookingRecordRepository repository = SpringHelper.getBean(BookingRecordRepository.class);
		Money priceFlight = this.flight.getCost();
		Money price = new Money();
		price.setCurrency(priceFlight.getCurrency());
		price.setAmount(priceFlight.getAmount() * repository.findCountPassengersBooking(this));
		return price;
	}


	@Mandatory
	@Automapped
	private boolean published;

}
