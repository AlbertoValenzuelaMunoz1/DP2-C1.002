
package acme.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidString;
import acme.datatypes.AircraftStatus;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Aircraft extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Automapped
	@Mandatory
	@ValidString(max = 50)
	@Column(nullable = false, length = 50)
	private String				model; // Aircraft model (up to 50 characters)

	@Automapped
	@Mandatory
	@ValidString(max = 50)
	@Column(unique = true, nullable = false, length = 50)
	private String				registrationNumber; // Unique registration number (up to 50 characters)

	@Automapped
	@Mandatory
	@Min(1)
	@Max(1000) // Assuming a reasonably high capacity limit for the number of passengers
	@Column(nullable = false)
	private int					passengerCapacity; // Capacity in number of passengers

	@Automapped
	@Mandatory
	@Min(2000) // Minimum weight of 2,000 kg
	@Max(50000) // Maximum weight of 50,000 kg
	@Column(nullable = false)
	private int					cargoWeight; // Cargo weight (between 2K and 50K kg)

	@Automapped
	@Mandatory
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AircraftStatus		status; // Status: ACTIVE or UNDER MAINTENANCE

	@Automapped
	@Optional
	@ValidString(max = 255)
	@Column(length = 255)
	private String				details; // Optional details (up to 255 characters)

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Automapped
	@Mandatory
	@ManyToOne(optional = false)
	private Airline				airline; // The airline that owns the aircraft
}
