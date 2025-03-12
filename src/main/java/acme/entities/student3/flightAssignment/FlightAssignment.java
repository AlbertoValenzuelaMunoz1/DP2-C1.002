
package acme.entities.student3.flightAssignment;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.datatypes.AssignmentStatus;
import acme.datatypes.FlightDuty;
import acme.entities.student1.leg.Leg;
import acme.entities.student3.flightCrewMember.FlightCrewMember;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightAssignment extends AbstractRole {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Automapped
	@Mandatory
	@Valid
	private FlightDuty			duty;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				lastUpdate;

	@Automapped
	@Mandatory
	@Valid
	private AssignmentStatus	status;

	@Automapped
	@Optional
	@ValidString(max = 255)
	private String				remarks;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private FlightCrewMember	flightCrewMember;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Leg					flightLeg;
}
