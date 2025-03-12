
package acme.entities.student4.tranckingLogs;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
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
import acme.entities.student4.claim.Claim;
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

	@Optional
	@Automapped
	private Boolean				claimAccepted;
	//
	//
	//	@Transient
	//	public boolean isResolved() {
	//		boolean result;
	//		result = this.belongsTo != null && this.belongsTo.getIndicator() != null;
	//		return result;
	//	}

	@Optional
	@Automapped
	@acme.Validators.ValidResolution
	private String				resolutionDetails;

	@Mandatory
	@ManyToOne
	@Valid
	private Claim				belongsTo;

}
