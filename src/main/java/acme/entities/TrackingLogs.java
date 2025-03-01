
package acme.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import acme.client.components.basis.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TrackingLogs extends AbstractEntity {

	//Serialisation -------------------------------

	private static final long	serialVersionUID	= 1L;

	//Attributes ---------------------------------

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				lastUpdateMoment;

	@NotBlank
	@Size(max = 50)
	private String				stepUndergoing;

	@Min(0)
	@Max(100)
	@NotNull
	private int					resolutionPercentage;

	private boolean				claimAccepted;

	@Size(max = 255)
	@Column(nullable = true)
	private String				resolutionDetails;
}
