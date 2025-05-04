
package acme.Validators;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.datatypes.IndicatorStatus;
import acme.entities.student4.tranckingLog.TrackingLog;

public class ResolutionValidator extends AbstractValidator<ValidResolution, String> {

	private TrackingLog trackingLog;


	public ResolutionValidator(final TrackingLog trackingLog) {
		this.trackingLog = trackingLog;
	}

	@Override
	public boolean isValid(final String resolutionDetails, final ConstraintValidatorContext context) {
		if (this.trackingLog == null)
			return true; // No se valida si el objeto es null

		IndicatorStatus claimAccepted = this.trackingLog.getClaimStatus();

		if (claimAccepted == null)
			return resolutionDetails == null; // Si claimAccepted es null, resolutionDetails también debe ser null
		else
			return resolutionDetails != null && !resolutionDetails.trim().isEmpty(); // Si claimAccepted no es null, resolutionDetails debe ser NotNull
	}
}
