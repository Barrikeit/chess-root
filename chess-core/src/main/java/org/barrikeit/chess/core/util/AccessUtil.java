package org.barrikeit.chess.core.util;

import lombok.extern.log4j.Log4j2;
import org.barrikeit.chess.core.util.annotations.AccessColumn;
import org.barrikeit.chess.core.util.model.AccessRow;
import org.barrikeit.chess.core.util.model.FieldAndInstance;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class AccessUtil {
  private AccessUtil() {
    throw new IllegalStateException("AccessUtil class");
  }

  public static AccessRow getAccessColumnFieldsByTable(Object instance, String accessTable) {
    List<FieldAndInstance> fieldsFilteredByTable =
        getFieldsWithAnnotationAccessColumns(instance, accessTable);

    int size = fieldsFilteredByTable.size();
    Object[] rowData = new Object[size];
    List<Integer> uniqueIndexesList = new ArrayList<>();
    for (FieldAndInstance fieldAndInstance : fieldsFilteredByTable) {
      // mirar el atributo column de la anotacion que identifica el orden de la columna en el
      // fichero Access
      Field field = fieldAndInstance.getField();
      AccessColumn annotation = field.getAnnotation(AccessColumn.class);
      int orderColumn = annotation.column();
      Object value = fieldAndInstance.getValueFromInstance();
      // Meter el Object, valor del campo segun su orden
      rowData[orderColumn] = value;
      // Mirar si la anotacion tiene el atributo unique
      boolean unique = annotation.unique();
      if (unique) {
        uniqueIndexesList.add(orderColumn);
      }
    }

    return new AccessRow(rowData, uniqueIndexesList);
  }

  public static List<FieldAndInstance> getFieldsWithAnnotationAccessColumns(
      Object instance, String accessTable) {
    List<FieldAndInstance> fieldsFilteredByTable = new ArrayList<>();
    for (Field field : instance.getClass().getDeclaredFields()) {
      if (field.isAnnotationPresent(AccessColumn.class)) {
        AccessColumn annotation = field.getAnnotation(AccessColumn.class);
        // Comprobar que la anotacion tiene un campo table igual al argumento del metodo
        if (annotation.name().equals(accessTable)) {
          Object fieldValue = ReflectionUtil.getFieldValue(instance, field.getName());
          // Si el field es un objeto Compuesto, llamada recursiva
          if (fieldValue != null
              && !field.getType().isPrimitive()
              && !field.getType().equals(String.class)
              && !field.getType().getName().startsWith("java")) {
            // Llamada recursiva
            fieldsFilteredByTable.addAll(
                getFieldsWithAnnotationAccessColumns(fieldValue, accessTable));
          }
          // Dato primitivo
          else {
            FieldAndInstance fieldAndInstance = new FieldAndInstance(field, instance);
            fieldsFilteredByTable.add(fieldAndInstance);
          }
        }
      }
    }
    return fieldsFilteredByTable;
  }
}
