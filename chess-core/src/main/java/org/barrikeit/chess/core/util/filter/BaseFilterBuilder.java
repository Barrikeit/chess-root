package org.barrikeit.chess.core.util.filter;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OrderColumn;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.barrikeit.chess.core.service.dto.GenericDto;
import org.barrikeit.chess.core.util.ReflectionUtil;
import org.barrikeit.chess.core.util.constants.ExceptionConstants;
import org.barrikeit.chess.core.util.constants.UtilConstants;
import org.barrikeit.chess.core.util.enums.SearchOperation;
import org.barrikeit.chess.core.util.exceptions.BadRequestException;
import org.barrikeit.chess.core.util.search.SearchCriteria;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public abstract class BaseFilterBuilder<D extends GenericDto, F extends BaseFilter> {
  private List<SearchCriteria> params = new ArrayList<>();
  private List<SearchCriteria> filters = new ArrayList<>();
  private Pageable page;

  protected BaseFilterBuilder(Pageable page, String search) {
    this.page = page;
    Pattern pattern = Pattern.compile(UtilConstants.EXPRESION_REGULAR_PARAMETROS);
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
      throw new BadRequestException(ExceptionConstants.ERROR_INVALID_SEARCH_OPERATION, operator);
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
            throw new BadRequestException(ExceptionConstants.ERROR_INVALID_FILTER, param.getKey());
          }
        });
  }

  /** Método que obtiene el objeto Sort que se utilizará para ordenar los resultados. */
  public Sort getSort() {
    List<Sort.Order> orders = new ArrayList<>();

    List<String> properties =
        ReflectionUtil.getFields(ReflectionUtil.getParameterizedTypeClass(this.getClass(), 1))
            .stream()
            .filter(item -> item.isAnnotationPresent(OrderColumn.class))
            .map(Field::getName)
            .toList();
    Sort sort = page.getSort();
    Iterator<Sort.Order> it = sort.stream().iterator();
    if (it.hasNext()) {
      Sort.Order order = it.next();
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
      throw new BadRequestException(ExceptionConstants.ERROR_INVALID_SORT, property);
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
