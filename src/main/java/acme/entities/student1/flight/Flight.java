
package acme.entities.student1.flight;

import java.beans.Transient;

import javax.persistence.Entity;

import org.dom4j.tree.AbstractEntity;

import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.entities.group.airport.Airport;
import acme.entities.student1.leg.LegRepository;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				tag;

	@Mandatory
	@Automapped
	private boolean				transfer;

	@Mandatory
	@ValidMoney
	private Money				cost;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				description;


	@Transient
	public Date scheduledDeparture() {

		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);

		return legRepository.firstFlightLeg(this).getScheduledDeparture();
	}

	@Transient
	private Date scheduledArrival() {

		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);

		return legRepository.lastFlightLeg(this).getScheduledArrival();
	}

	@Transient
	public Airport originCity() {

		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);

		return legRepository.firstFlightLeg(this).getDepartureAirport();
	}

	@Transient
	public Airport destinationCity() {

		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);

		return legRepository.lastFlightLeg(this).getArrivalAirport();
	}

	@Transient
	public Integer numberOfLayovers() {

		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);

		return legRepository.numberOfLegs(this).intValue();
	}

}
