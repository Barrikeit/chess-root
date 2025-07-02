package org.barrikeit.chess.core.util;

import lombok.extern.log4j.Log4j2;
import org.barrikeit.chess.core.util.annotations.ExcelColumn;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;

@Log4j2
public class ExcelUtil {
  private ExcelUtil() {
    throw new IllegalStateException("ExcelUtil class");
  }

  public static List<Field> getExcelColumns(Class<?> clazz, boolean template) {
    return ReflectionUtil.getFieldsWithAnnotation(clazz, ExcelColumn.class).stream()
        .filter((field -> (!template || field.getAnnotation(ExcelColumn.class).template())))
        .sorted(Comparator.comparingInt(field -> field.getAnnotation(ExcelColumn.class).column()))
        .toList();
  }
}
