
package acme.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.PastOrPresent;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ActivityLog extends AbstractRole {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	//--------------------------------------------------;

	@Automapped
	@Mandatory
	@ManyToOne(optional = false)
	@JoinColumn(name = "flight_assignment_id", nullable = false)
	private FlightAssignment	flightAssignment;

	//	@Automapped
	//	@Mandatory
	//	@ManyToOne(optional = false)
	//	@JoinColumn(name = "flight_leg_id", nullable = false)
	//	private FlightLeg			flightLeg;

	@Automapped
	@Mandatory
	@PastOrPresent
	@Column(nullable = false)
	private LocalDateTime		registrationMoment; // Must be in the past or present

	@Automapped
	@Mandatory
	@ValidString(max = 50)
	@Column(nullable = false, length = 50)
	private String				incidentType; // Up to 50 characters

	@Automapped
	@Mandatory
	@ValidString(max = 255)
	@Column(nullable = false, length = 255)
	private String				description; // Up to 255 characters

	@Automapped
	@Mandatory
	@Min(0)
	@Max(10)
	@Column(nullable = false)
	private int					severity; // Range from 0 (no issue) to 10 (critical)
}
