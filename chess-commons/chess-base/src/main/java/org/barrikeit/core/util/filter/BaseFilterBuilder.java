package org.barrikeit.core.util.filter;

import static org.barrikeit.core.util.constants.ApplicationConstants.EXPRESION_REGULAR_PARAMETROS;
import static org.barrikeit.core.util.constants.ErrorConstants.ERROR_INVALID_FILTER;
import static org.barrikeit.core.util.constants.ErrorConstants.ERROR_INVALID_SEARCH_OPERATION;
import static org.barrikeit.core.util.constants.ErrorConstants.ERROR_INVALID_SORT;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OrderColumn;
import jakarta.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.barrikeit.core.error.BadRequestException;
import org.barrikeit.core.rest.dto.BaseDto;
import org.barrikeit.core.util.ReflectionUtil;
import org.barrikeit.core.util.search.SearchCriteria;
import org.barrikeit.core.util.enums.SearchOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

/**
 * Clase abstracta BaseFilterBuilder que se utiliza para construir filtros personalizados. Esta
 * clase se mapea como una superclase para las entidades que necesitan implementar la funcionalidad
 * de filtrado.
 *
 * @param <D> DTO que se utilizará para comprobar las propiedades de la clase BaseFilter para la
 *     validación de los filtros
 * @param <F> Clase que posee las propiedades que se realizarán el filtrado de los datos.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class BaseFilterBuilder<D extends BaseDto, F extends BaseFilter> {

  protected static final int FILTER_CLASS_INDEX = 1;
  private List<SearchCriteria> params = new ArrayList<>();
  private List<SearchCriteria> filters = new ArrayList<>();
  private Pageable page;

  protected BaseFilterBuilder(Pageable page, String search) {
    this.page = page;
    Pattern pattern = Pattern.compile(EXPRESION_REGULAR_PARAMETROS);
    Matcher matcher = pattern.matcher((!ObjectUtils.isEmpty(search) ? search : "") + ",");
    while (matcher.find()) {
      with(matcher.group(1), matcher.group(2), matcher.group(3));
    }
    F filter = customFilter(filters);
    validateFilter(filter);
    this.page = PageRequest.of(page.getPageNumber(), page.getPageSize(), getSort());
  }

  /**
   * Método abstracto que debe ser implementado por las subclases para construir un filtro
   * personalizado de las propiedades de la @Entity
   */
  public abstract F customFilter(List<SearchCriteria> params);

  /** Método que añade un nuevo criterio de búsqueda a la lista de criterios. */
  public BaseFilterBuilder<D, F> with(
      @NotNull final String key, @NotNull String operator, @NotNull final Object value) {
    SearchOperation searchOperation =
        !ObjectUtils.isEmpty(operator) && operator.length() == 1
            ? SearchOperation.getSimpleOperation(operator.charAt(0))
            : null;

    if (null == searchOperation) {
      throw new BadRequestException(ERROR_INVALID_SEARCH_OPERATION, operator);
    }
    if (!ObjectUtils.isEmpty(key) && !ObjectUtils.isEmpty(value)) {
      params.add(
          SearchCriteria.builder().key(key.trim()).operation(searchOperation).value(value).build());
      filters.add(
          SearchCriteria.builder().key(key.trim()).operation(searchOperation).value(value).build());
    }

    return this;
  }

  /** Método privado que valida las propiedades del filtro. */
  private void validateFilter(F filter) throws BadRequestException {

    List<String> properties =
        Arrays.stream(filter.getClass().getDeclaredFields()).map(Field::getName).toList();
    params.forEach(
        param -> {
          if (!properties.contains(param.getKey())) {
            throw new BadRequestException(ERROR_INVALID_FILTER, param.getKey());
          }
        });
  }

  /** Método que obtiene el objeto Sort que se utilizará para ordenar los resultados. */
  public Sort getSort() {
    List<Sort.Order> orders = new ArrayList<>();

    List<String> properties =
        Arrays.stream(
                ReflectionUtil.getAllFields(
                    ReflectionUtil.getClass(this.getClass(), FILTER_CLASS_INDEX)))
            .filter(item -> item.isAnnotationPresent(OrderColumn.class))
            .map(Field::getName)
            .toList();
    Sort sort = page.getSort();
    Iterator<Sort.Order> it = sort.stream().iterator();
    if (it.hasNext()) {
      Sort.Order order = it.next(); // primer elemento
      String property = order.getProperty();
      verifyPropertyToOrder(properties, property, orders, order);
    }
    return Sort.by(orders);
  }

  /**
   * Método que verifica si una propiedad puede ser ordenada y, en caso afirmativo, añade la orden a
   * la lista de órdenes.
   */
  private void verifyPropertyToOrder(
      List<String> properties, String property, List<Sort.Order> orders, Sort.Order order) {
    if (properties.contains(property)) {
      orders.add(assignPropertyOrder(property, order));
    } else {
      throw new BadRequestException(ERROR_INVALID_SORT, property);
    }
  }

  /**
   * Este método se utiliza para asignar una propiedad a un objeto Sort.Order. La propiedad y el
   * objeto Sort.Order se pasan como parámetros.
   *
   * @param property La propiedad que se va a ordenar. Debe ser una propiedad válida de la entidad.
   * @param order El objeto Sort.Order que contiene la dirección de la ordenación (ascendente o
   *     descendente).
   * @return Un nuevo objeto Sort.Order con la propiedad y la dirección de ordenación asignadas.
   */
  public Sort.Order assignPropertyOrder(String property, Sort.Order order) {
    return new Sort.Order(order.getDirection(), property);
  }
}
