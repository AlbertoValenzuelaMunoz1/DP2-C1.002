
package acme.constraints;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;

public class ValueValidator extends AbstractValidator<ValidValue, String> {

	private List<String> values;


	@Override
	public void initialise(final ValidValue annotation) {
		this.values = Arrays.asList(annotation.values());
	}
	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		return value == null || this.values.contains(value);
	}

}
