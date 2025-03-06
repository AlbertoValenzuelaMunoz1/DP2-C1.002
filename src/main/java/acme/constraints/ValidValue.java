
package acme.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({
	ElementType.FIELD, ElementType.ANNOTATION_TYPE
})
@Retention(RetentionPolicy.RUNTIME)
@Documented

@Constraint(validatedBy = {
	ValueValidator.class
})
public @interface ValidValue {

	String message() default "{acme.validation.object.message}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	String[] values() default {};
}
