
package acme.entities.student1.flight;

import java.beans.Transient;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.entities.group.airport.Airport;
import acme.entities.student1.leg.Leg;
import acme.entities.student1.leg.LegRepository;
import acme.entities.student1.manager.Manager;
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

	@Mandatory
	@Automapped
	private boolean				draftMode;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				description;


	@Transient
	public Date scheduledDeparture() {

		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);

		List<Leg> legs = legRepository.firstFlightLeg(this);

		Leg leg = legs == null || legs.isEmpty() ? null : legs.get(0);

		Date date = leg == null ? null : leg.getScheduledDeparture();

		return date;
	}

	@Transient
	public Date scheduledArrival() {

		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);

		List<Leg> legs = legRepository.lastFlightLeg(this);

		Leg leg = legs == null || legs.isEmpty() ? null : legs.get(0);

		System.out.println(leg);

		Date date = leg == null ? null : leg.getScheduledArrival();

		return date;
	}

	@Transient
	public Airport originCity() {

		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);

		List<Leg> legs = legRepository.firstFlightLeg(this);

		Leg leg = legs == null || legs.isEmpty() ? null : legs.get(0);

		Airport airport = leg == null ? null : leg.getDepartureAirport();

		return airport;
	}

	@Transient
	public Airport destinationCity() {

		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);

		List<Leg> legs = legRepository.lastFlightLeg(this);

		Leg leg = legs == null || legs.isEmpty() ? null : legs.get(0);

		Airport airport = leg == null ? null : leg.getArrivalAirport();

		return airport;
	}

	@Transient
	public Integer numberOfLayovers() {

		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);
		Long result = legRepository.numberOfLegs(this);
		return result != null ? result.intValue() : 0;
	}


	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Manager manager;

}
