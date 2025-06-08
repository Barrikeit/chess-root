package org.barrikeit.chess.core.util.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.barrikeit.chess.domain.model.base.GenericEntity;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AccessTable {
  String name() default "";

  Class<?> entity() default GenericEntity.class;
}
