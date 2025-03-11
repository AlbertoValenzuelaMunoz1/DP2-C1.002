
package acme.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.Year;
import java.util.regex.Pattern;

import javax.validation.Constraint;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPromotionCode.PromotionCodeValidator.class)
public @interface ValidPromotionCode {

	String message() default "Código promocional no válido";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};


	@Validator
	public static class PromotionCodeValidator extends AbstractValidator<ValidPromotionCode, String> {

		private static final Pattern PROMO_CODE_PATTERN = Pattern.compile("^[A-Z]{4}-[0-9]{2}$");


		@Override
		public boolean isValid(final String promotionCode, final ConstraintValidatorContext context) {
			if (promotionCode == null)
				return true; // El código es opcional, se permite null

			if (!PromotionCodeValidator.PROMO_CODE_PATTERN.matcher(promotionCode).matches()) {
				context.buildConstraintViolationWithTemplate("El formato debe ser AAAA-99").addConstraintViolation();
				return false;
			}

			try {
				int añoActual = Year.now().getValue() % 100;
				int añoCodigo = Integer.parseInt(promotionCode.substring(5));

				if (añoCodigo != añoActual && añoCodigo != añoActual - 1) {
					context.buildConstraintViolationWithTemplate("Los últimos dígitos deben ser el año actual o el anterior").addConstraintViolation();
					return false;
				}
			} catch (NumberFormatException e) {
				context.buildConstraintViolationWithTemplate("Error al procesar el código promocional").addConstraintViolation();
				return false;
			}

			return true;
		}
	}
}
