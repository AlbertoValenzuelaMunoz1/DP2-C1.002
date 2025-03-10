
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.datatypes.AssignmentStatus;
import acme.datatypes.FlightDuty;
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
	@Enumerated(EnumType.STRING)
	private FlightDuty			duty;

	@Automapped
	@Mandatory
	@ValidMoment(past = true)
	private Date				lastUpdate;

	@Automapped
	@Mandatory
	@Enumerated(EnumType.STRING)
	private AssignmentStatus	status;

	@Automapped
	@Optional
	@ValidString(max = 255)
	private String				remarks;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Automapped
	@Mandatory
	@ManyToOne(optional = false)
	private FlightCrewMember	flightCrewMember;

	@Automapped
	@Mandatory
	@ManyToOne(optional = false)
	private Leg					flightLeg;
}
