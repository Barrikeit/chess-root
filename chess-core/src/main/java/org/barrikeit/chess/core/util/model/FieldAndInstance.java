package org.barrikeit.chess.core.util.model;

import lombok.Getter;
import lombok.Setter;
import org.barrikeit.chess.core.util.ReflectionUtil;

import java.lang.reflect.Field;

/**
 * Clase util para almacenar un java.lang.refelect.Field y su instancia. Sirve para recuperar el
 * valor de una propiedad definida por el metadato Field a partir del objeto instancia original
 */
@Getter
@Setter
public class FieldAndInstance {
  private Field field; // solo son metadatos
  private Object instance; // objeto con el valor real del campo

  public FieldAndInstance(Field field, Object instance) {
    this.field = field;
    this.instance = instance;
  }

  public Object getValueFromInstance() {
    return ReflectionUtil.getFieldValue(instance, field.getName());
  }
}
