
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.datatypes.IndicatorStatus;
import acme.entities.student4.claim.Claim;
import acme.entities.student4.tranckingLog.TrackingLog;
import acme.features.agent.claim.ClaimRepository;

@Validator
public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	private final ClaimRepository claimRepository;


	@Autowired
	public TrackingLogValidator(final ClaimRepository claimRepository) {
		this.claimRepository = claimRepository;
	}

	@Override
	protected void initialise(final ValidTrackingLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {
		// HINT: trackingLog can be null
		assert context != null;

		if (trackingLog == null)
			return true;

		Claim claim = trackingLog.getClaim();
		List<TrackingLog> trackingLogs = this.claimRepository.getTrackingLogsByResolutionOrder(claim.getId());
		if (!trackingLogs.isEmpty()) {
			TrackingLog highestTrackingLog = trackingLogs.get(0);
			if (trackingLog.getResolutionPercentage() < highestTrackingLog.getResolutionPercentage()) {
				String errorMessage = String.format("Resolution percentage %.2f must be higher than the highest resolution percentage %.2f for claim ID %d", trackingLog.getResolutionPercentage(), highestTrackingLog.getResolutionPercentage(),
					claim.getId());
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate(errorMessage).addPropertyNode("resolPercentage").addConstraintViolation();
				return false;
			}
		}

		boolean result;
		boolean isNull;

		isNull = trackingLog == null || trackingLog.getResolutionPercentage() == null || trackingLog.getClaimStatus() == null;

		if (!isNull) {
			{
				Double resolPercentage;
				boolean validPercentage;

				resolPercentage = trackingLog.getResolutionPercentage();
				validPercentage = resolPercentage >= 0.0 && resolPercentage <= 100.0;
				super.state(context, validPercentage, "resolPercentage", "acme.validation.trackingLog.resolPercentage.message");
			}
			{
				IndicatorStatus status;
				boolean validStatus;

				status = trackingLog.getClaimStatus();
				if (trackingLog.getResolutionPercentage() == 100.0)
					validStatus = status == IndicatorStatus.ACCEPTED || status == IndicatorStatus.REJECTED;
				else
					validStatus = status == IndicatorStatus.PENDING;
				super.state(context, validStatus, "status", "acme.validation.trackingLog.status.message");
			}
			{
				String resolution;
				boolean validResolution;

				resolution = trackingLog.getResolutionDetails();
				if (trackingLog.getResolutionPercentage() == 100.0)
					validResolution = resolution != null && !resolution.isEmpty();
				else
					validResolution = true;
				super.state(context, validResolution, "resolution", "acme.validation.trackingLog.resolution.message");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}
}
