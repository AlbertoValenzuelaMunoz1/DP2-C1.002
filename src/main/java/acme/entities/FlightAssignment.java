
package acme.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.PastOrPresent;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
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

	//--------------------------------------------------

	@Automapped
	@Mandatory
	@ManyToOne(optional = false)
	@JoinColumn(name = "flight_crew_member_id", nullable = false)
	private FlightCrewMember	flightCrewMember;

	//	@Automapped
	//	@Mandatory
	//	@ManyToOne(optional = false)
	//	@JoinColumn(name = "flight_leg_id", nullable = false)
	//	private FlightLeg			flightLeg;

	@Automapped
	@Mandatory
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private FlightDuty			duty; // "PILOT", "CO-PILOT", "LEAD ATTENDANT", "CABIN ATTENDANT"

	@Automapped
	@Mandatory
	@PastOrPresent
	@Column(nullable = false)
	private LocalDateTime		lastUpdate; // Must be in the past or present

	@Automapped
	@Mandatory
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AssignmentStatus	status; // "CONFIRMED", "PENDING", "CANCELLED"

	@Automapped
	@Optional
	@ValidString(max = 255)
	private String				remarks; // Optional, max 255 characters
}
