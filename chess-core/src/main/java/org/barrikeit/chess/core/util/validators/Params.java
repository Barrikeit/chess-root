package org.barrikeit.chess.core.util.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ParamsValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Params {
  String message() default "error.msg.validators.params";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
