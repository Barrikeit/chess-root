package org.barrikeit.core.util.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;
import org.barrikeit.core.util.annotations.validators.AlphanumericValidator;

@Constraint(validatedBy = AlphanumericValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Alphanumeric {
  String message() default "error.msg.validation.alphanumeric.1";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
