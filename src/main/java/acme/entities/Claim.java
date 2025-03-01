
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.datatypes.ClaimType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Claim extends AbstractEntity {

	//Serialisation -------------------------------

	private static final long	serialVersionUID	= 1L;

	//Attributes ---------------------------------

	@Mandatory
	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@Mandatory
	@NotBlank
	@Email
	private String				passengerEmail;

	@Mandatory
	@NotBlank
	@Size(max = 255)
	private String				description;

	@Mandatory
	@NotNull
	@Enumerated(EnumType.STRING)
	private ClaimType			type;

	@Mandatory
	@NotNull
	private boolean				accepted;

	@Mandatory
	@NotNull
	@ManyToOne
	@JoinColumn(name = "assistanceAgents_id", nullable = false)
	private AssistanceAgents	registredBy;

	//posible asociacion con passenger o consumer 

}
