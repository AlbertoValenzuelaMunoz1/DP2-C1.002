
package acme.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidScore;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TrackingLogs extends AbstractEntity {

	//Serialisation -------------------------------

	private static final long	serialVersionUID	= 1L;

	//Attributes ---------------------------------

	@Mandatory
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment
	private Date				lastUpdateMoment;

	@Mandatory
	@NotBlank
	@ValidString(max = 50)
	@Automapped
	private String				stepUndergoing;

	@Mandatory
	@NotNull
	@ValidScore
	@Automapped
	private Double				resolutionPercentage;

	@Mandatory
	@Automapped
	private Boolean				claimAccepted;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				resolutionDetails;

	@Mandatory
	@NotNull
	@OneToOne
	@JoinColumn(name = "claim_id", nullable = false)
	@Valid
	private Claim				belongsTo;

}
