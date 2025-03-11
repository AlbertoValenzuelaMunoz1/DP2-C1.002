
package acme.entities;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Moment;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;

public class Leg extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(max = 10)
	@Automapped
	private String				flightNumber;

	@Mandatory
	@ValidMoment(past = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Moment				scheduledDeparture;

	@Mandatory
	@ValidMoment(past = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Moment				scheduledArrival;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIME)
	@Automapped
	private Moment				duration;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private FlightStatus		status;

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

	@ManyToOne
	@JoinColumn(name = "flight_id", nullable = false)
	private Flight				flight;


	public enum FlightStatus {
		ON_TIME, DELAYED, CANCELLED, LANDED
	}

}
