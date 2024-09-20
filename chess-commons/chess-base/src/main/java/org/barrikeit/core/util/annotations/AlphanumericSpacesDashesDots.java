package org.barrikeit.core.util.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.barrikeit.core.util.annotations.validators.AlphanumericSpacesDashesDotsValidator;

@Constraint(validatedBy = AlphanumericSpacesDashesDotsValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AlphanumericSpacesDashesDots {
  String message() default "error.msg.validation.alphanumeric.3";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
