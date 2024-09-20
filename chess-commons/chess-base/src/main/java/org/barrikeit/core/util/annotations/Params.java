package org.barrikeit.core.util.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.barrikeit.core.util.annotations.validators.ParamsValidator;

@Constraint(validatedBy = ParamsValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Params {
  String message() default "error.msg.validation.params";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
