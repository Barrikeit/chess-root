package org.barrikeit.chess.core.util.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AccessColumn {
  String name() default "";

  int column() default -1;

  boolean unique() default false;
}
