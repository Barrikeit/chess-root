package org.barrikeit.chess.core.util.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AlphanumericValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Alphanumeric {
  String message() default "error.msg.validators.alphanumeric.1";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
