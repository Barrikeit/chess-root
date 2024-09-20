package org.barrikeit.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionUtil {
  private ReflectionUtil() {}

  public static <E> Class<E> getClass(Class<E> clazz, int index) {
    ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericSuperclass();
    Type[] typeArguments = parameterizedType.getActualTypeArguments();
    @SuppressWarnings("unchecked")
    Class<E> tClass = (Class<E>) typeArguments[index];
    return tClass;
  }

  public static Field[] getAllFields(Class<?> clazz) {
    List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));

    Class<?> superClass = clazz.getSuperclass();
    if (superClass != null) {
      fields.addAll(Arrays.asList(getAllFields(superClass)));
    }

    return fields.toArray(new Field[0]);
  }

  public static Method getGetterMethod(Class<?> clazz, String fieldName)
      throws NoSuchMethodException {
    return clazz.getMethod(
        "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
  }
}
