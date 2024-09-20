package org.barrikeit.core.util.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.barrikeit.core.util.annotations.validators.SanitizeValidator;

@Constraint(validatedBy = SanitizeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Sanitize {
  String message() default "error.msg.validation.sanitize";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
