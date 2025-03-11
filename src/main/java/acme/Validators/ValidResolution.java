
package acme.Validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = ResolutionValidator.class)
@Target({
	ElementType.FIELD
})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidResolution {

	int min() default 0;
	int max() default 255;
	String pattern() default "";

	String message() default "Resolution must be null if claim is unresolved, and mandatory if resolved.";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
