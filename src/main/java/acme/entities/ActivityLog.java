
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ActivityLog extends AbstractRole {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Automapped
	@Mandatory
	@ValidMoment
	private Date				registrationMoment;

	@Automapped
	@Mandatory
	@ValidString(max = 50)
	private String				incidentType;

	@Automapped
	@Mandatory
	@ValidString(max = 255)
	private String				description;

	@Automapped
	@Mandatory
	@ValidNumber(min = 0, max = 10)
	private int					severity;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Automapped
	@Mandatory
	@ManyToOne(optional = false)
	private FlightAssignment	flightAssignment;
}
