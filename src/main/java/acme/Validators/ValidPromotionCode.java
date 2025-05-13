
package acme.Validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

import javax.validation.Constraint;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPromotionCode.PromotionCodeValidator.class)
public @interface ValidPromotionCode {

	String message() default "acme.validation.promotion-code.invalid";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};


	@Validator
	class PromotionCodeValidator extends AbstractValidator<ValidPromotionCode, String> {

		private static final Pattern PROMO_CODE_PATTERN = Pattern.compile("^[A-Z]{4}-[0-9]{2}$");


		@Override
		public void initialize(final ValidPromotionCode annotation) {
			assert annotation != null;
		}

		@Override
		public boolean isValid(final String promotionCode, final ConstraintValidatorContext context) {
			assert context != null;

			if (promotionCode == null)
				return true; // Se permite null, es opcional

			boolean patternMatched = PromotionCodeValidator.PROMO_CODE_PATTERN.matcher(promotionCode).matches();
			super.state(context, patternMatched, "*", "acme.validation.promotion-code.mismatch");

			boolean lastTwoDigitsMatchYear = false;
			try {
				@SuppressWarnings("deprecation")
				int year = MomentHelper.getCurrentMoment().getYear() % 100;
				String lastTwoDigitsString = promotionCode.substring(5);
				int lastTwoDigits = Integer.parseInt(lastTwoDigitsString);
				lastTwoDigitsMatchYear = lastTwoDigits == year;
			} catch (NumberFormatException | StringIndexOutOfBoundsException e) {
				lastTwoDigitsMatchYear = false;
			}

			super.state(context, lastTwoDigitsMatchYear, "*", "acme.validation.promotion-code.year-mismatch");
			return !super.hasErrors(context);
		}
	}
}
