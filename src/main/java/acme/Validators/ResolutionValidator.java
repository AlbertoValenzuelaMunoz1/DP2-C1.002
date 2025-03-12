
package acme.Validators;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.entities.student4.tranckingLogs.TrackingLogs;

public class ResolutionValidator extends AbstractValidator<ValidResolution, String> {

	@Override
	public boolean isValid(final String resolutionDetails, final ConstraintValidatorContext context) {

		Object trackingLogObject = context.unwrap(TrackingLogs.class);

		if (!(trackingLogObject instanceof TrackingLogs))
			return true;

		TrackingLogs trackingLog = (TrackingLogs) trackingLogObject;

		if (trackingLog.getBelongsTo() == null)
			return true;

		Boolean indicator = trackingLog.getClaimAccepted();

		if (indicator == null)
			return resolutionDetails == null; // Si Claim no está aceptado/rechazado debe ser null
		else
			return resolutionDetails != null && !resolutionDetails.trim().isEmpty(); // Si está aceptado/rechazado resolutionDetails debe ser obligatorio
	}
}

//Hice esta opcion pero no me dejaba aplicarla al atributo directamente

//public class ResolutionValidator extends AbstractValidator<ValidResolution, TrackingLogs> {
//
//@Override
//public boolean isValid(final TrackingLogs trackingLog, final ConstraintValidatorContext context) {
//if (trackingLog == null || trackingLog.getBelongsTo() == null)
//return true;
//
//Boolean indicator = trackingLog.getBelongsTo().getIndicator();
//String resolutionDetails = trackingLog.getResolutionDetails();
//
//if (indicator == null)
//return resolutionDetails == null; // Si el claim no está aceptado/rechazado debe ser null
//else
//return resolutionDetails != null && !resolutionDetails.trim().isEmpty(); // Si está aceptado/rechazado debe ser obligatorio
//}
//}
