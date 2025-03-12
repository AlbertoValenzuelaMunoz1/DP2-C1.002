
package acme.Validators;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.PropertyHelper;
import acme.client.helpers.StringHelper;
import acme.internals.helpers.HibernateHelper;

public class PhoneNumberValidator extends AbstractValidator<ValidPhoneNumber, String> {

	// Internal state ---------------------------------------------------------

	private String phonePattern;

	// Initialiser ------------------------------------------------------------


	@Override
	public void initialise(final ValidPhoneNumber annotation) {
		assert annotation != null;
		this.phonePattern = PropertyHelper.getRequiredProperty("acme.data.phone.pattern", String.class);
	}

	// ConstraintValidator interface -----------------------------------------

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (StringHelper.isBlank(value))
			result = true;
		else {
			result = Pattern.matches(this.phonePattern, value);

			if (!result)
				HibernateHelper.replaceParameter(context, "placeholder", "acme.validation.phonenumber.message");
		}

		return result;
	}
}
