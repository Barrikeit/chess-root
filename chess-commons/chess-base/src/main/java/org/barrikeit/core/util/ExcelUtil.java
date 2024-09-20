package org.barrikeit.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.barrikeit.core.error.BadRequestException;
import org.barrikeit.core.error.NotFoundException;
import org.barrikeit.core.util.annotations.ExcelColumn;
import org.barrikeit.core.util.annotations.ExcelFile;

public class ExcelUtil {
  private ExcelUtil() {
    throw new IllegalStateException("Utility class");
  }

  public static List<String> getExcelNames(Class<?> clazz) {
    try {
      List<String> names = new ArrayList<>();
      Object obj = clazz.getDeclaredConstructor().newInstance();
      if (obj.getClass().isAnnotationPresent(ExcelFile.class)) {
        String fileName = obj.getClass().getAnnotation(ExcelFile.class).file();
        String sheetName = obj.getClass().getAnnotation(ExcelFile.class).sheet();
        names.add(fileName);
        names.add(sheetName);
      } else {
        throw new NotFoundException("error.msg.excel.annotation.excelFile" + clazz);
      }
      return names;
    } catch (NoSuchMethodException e) {
      throw new BadRequestException("error.msg.excel.class.constructor" + clazz);
    } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
      throw new BadRequestException("error.msg.excel.class.instance" + clazz);
    }
  }

  public static List<Field> getExcelColumns(Class<?> clazz) {
    List<Field> annotatedFields = new ArrayList<>();

    for (Field field : ReflectionUtil.getAllFields(clazz)) {
      if (field.isAnnotationPresent(ExcelColumn.class)) {
        annotatedFields.add(field);
      }
    }

    if (annotatedFields.isEmpty())
      throw new NotFoundException("error.msg.excel.annotation.excelColumn" + clazz);

    return annotatedFields.stream()
        .sorted(Comparator.comparingInt(field -> field.getAnnotation(ExcelColumn.class).column()))
        .toList();
  }
}
