
package acme.entities.student4.claim;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidClaim;
import acme.datatypes.ClaimType;
import acme.datatypes.IndicatorStatus;
import acme.entities.student1.leg.Leg;
import acme.entities.student4.tranckingLog.TrackingLog;
import acme.features.agent.trackingLog.TrackingLogRepository;
import acme.realms.AssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidClaim
public class Claim extends AbstractEntity {

	//Serialisation -------------------------------

	private static final long	serialVersionUID	= 1L;

	//Attributes ---------------------------------

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(past = true)
	private Date				registrationMoment;

	@Mandatory
	@ValidEmail
	@Automapped
	private String				passengerEmail;

	@Mandatory
	@ValidString(min = 1, max = 255)
	@Automapped
	private String				description;

	@Mandatory
	@Automapped
	@Valid
	private ClaimType			type;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private AssistanceAgent		assistanceAgent;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Leg					leg;


	@Transient
	public IndicatorStatus getIndicator() {
		TrackingLogRepository repository;
		repository = SpringHelper.getBean(TrackingLogRepository.class);
		TrackingLog topLog = repository.findFirstByClaimIdOrderByResolutionPercentageDesc(this.getId());

		return topLog != null ? topLog.getClaimStatus() : IndicatorStatus.PENDING;
	}

}
