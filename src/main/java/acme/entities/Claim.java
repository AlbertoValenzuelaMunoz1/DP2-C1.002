
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
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
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(past = true)
	private Date				registrationMoment;

	@Mandatory
	@NotBlank
	@ValidEmail
	@Automapped
	private String				passengerEmail;

	@Mandatory
	@NotBlank
	@ValidString(min = 1, max = 255)
	@Automapped
	private String				description;

	@Mandatory
	@NotNull
	@Enumerated(EnumType.STRING)
	@Valid
	@Automapped
	private ClaimType			type;

	@Optional
	@Automapped
	public Boolean				indicator;

	@Mandatory
	@NotNull
	@ManyToOne
	@JoinColumn(name = "assistanceAgents_id", nullable = false)
	@Valid
	private AssistanceAgents	registredBy;

}
