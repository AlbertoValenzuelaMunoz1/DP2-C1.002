
package acme.entities.student2.booking;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.entities.student2.passenger.Passenger;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BookingRecord extends AbstractEntity {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Booking				booking;

	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Passenger			passenger;

}
