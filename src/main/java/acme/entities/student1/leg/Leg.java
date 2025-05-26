
package acme.entities.student1.leg;

import java.beans.Transient;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.entities.group.aircraft.Aircraft;
import acme.entities.group.airport.Airport;
import acme.entities.student1.flight.Flight;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = @Index(columnList = "scheduledArrival,draftMode")) //Indice consulta que toma las legs que cumplen los requisitos 
public class Leg extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Column(unique = true)
	@ValidString(pattern = "^[0-9]{4}$")
	private String				flightNumberDigits;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledDeparture;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledArrival;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport				departureAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport				arrivalAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Aircraft			aircraft;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight				flight;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private LegStatus			status;

	@Mandatory
	@Automapped
	boolean						draftMode;


	@Transient
	public Long durationInHours() {

		Long hours = null;

		if (this.scheduledDeparture != null && this.scheduledArrival != null) {
			Instant departure = this.scheduledDeparture.toInstant();
			Instant arrival = this.scheduledArrival.toInstant();

			Duration time = Duration.between(departure, arrival);
			hours = time.toHours();
		}

		return hours;
	}

}
