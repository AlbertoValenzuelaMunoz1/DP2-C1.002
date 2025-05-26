
package acme.entities.student4.tranckingLog;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidScore;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidTrackingLog;
import acme.datatypes.IndicatorStatus;
import acme.entities.student4.claim.Claim;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidTrackingLog
@Table(indexes = @Index(columnList = "claim_id,resolutionPercentage"))
public class TrackingLog extends AbstractEntity {

	//Serialisation -------------------------------

	private static final long	serialVersionUID	= 1L;

	//Attributes ---------------------------------

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment
	private Date				lastUpdateMoment;

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				stepUndergoing;

	@Mandatory
	@ValidScore
	@Automapped
	private Double				resolutionPercentage;

	@Mandatory
	@Automapped
	@Valid
	public IndicatorStatus		claimStatus;

	@Optional
	@Automapped
	@ValidString
	private String				resolutionDetails;

	@Mandatory
	@Automapped
	boolean						draftMode;

	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Claim				claim;

}
