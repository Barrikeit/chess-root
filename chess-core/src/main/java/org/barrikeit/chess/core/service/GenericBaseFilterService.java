package org.barrikeit.chess.core.service;

import jakarta.persistence.criteria.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.barrikeit.chess.core.service.dto.GenericDto;
import org.barrikeit.chess.core.service.mapper.GenericMapper;
import org.barrikeit.chess.core.util.ReflectionUtil;
import org.barrikeit.chess.core.util.constants.ExceptionConstants;
import org.barrikeit.chess.core.util.exceptions.BadRequestException;
import org.barrikeit.chess.core.util.filter.BaseFilter;
import org.barrikeit.chess.core.util.filter.BaseFilterBuilder;
import org.barrikeit.chess.core.util.filter.FilterSpecification;
import org.barrikeit.chess.core.util.search.SearchCriteria;
import org.barrikeit.chess.domain.model.base.GenericEntity;
import org.barrikeit.chess.domain.repository.base.GenericRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

public abstract class GenericBaseFilterService<
        E extends GenericEntity<I>,
        I extends Serializable,
        D extends GenericDto,
        F extends BaseFilter,
        R extends GenericRepository<E, I>,
        M extends GenericMapper<E, D>>
    extends GenericService<E, I, D> {

  private final R repository;
  private final M mapper;

  protected GenericBaseFilterService(R repository, M mapper) {
    super(repository, mapper);
    this.repository = repository;
    this.mapper = mapper;
  }

  private static <T extends GenericEntity<? extends Serializable>>
      Specification<T> getSpecification(SearchCriteria param) {
    return new FilterSpecification<>(param) {
      @Override
      public Predicate toPredicate(
          Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        if (null == getSearchCriteria()) {
          return null;
        }
        String[] parts = getSearchCriteria().getKey().split("\\.");
        Join<T, Object> join = root.join(parts[0], JoinType.INNER);

        String attributeValue = getSearchCriteria().getValue().toString();

        return criteriaBuilder.like(
            criteriaBuilder.lower(join.get(parts[1]).as(String.class)),
            "%" + attributeValue.toLowerCase() + "%");
      }
    };
  }

  public abstract BaseFilterBuilder<D, F> instanceFilterBuilder(Pageable page, String search);

  /**
   * Realiza una búsqueda paginada o no paginada en base a los criterios de búsqueda proporcionados.
   *
   * @param page El objeto Pageable que contiene la información de paginación.
   * @param unpaged Un booleano que indica si la búsqueda debe ser paginada o no.
   * @param search Una cadena de texto que contiene los criterios de búsqueda.
   * @return Una página de DTOs que cumplen con los criterios de búsqueda.
   */
  public Page<D> search(@NotNull Pageable page, boolean unpaged, String search) {
    Page<E> paged = searchEntity(page, unpaged, search);
    List<D> result = paged.getContent().stream().map(mapper::toDto).toList();
    return new PageImpl<>(result, paged.getPageable(), paged.getTotalElements());
  }

  /**
   * Realiza una búsqueda paginada o no paginada en base a los criterios de búsqueda proporcionados.
   *
   * @param page El objeto Pageable que contiene la información de paginación.
   * @param unpaged Un booleano que indica si la búsqueda debe ser paginada o no.
   * @param search Una cadena de texto que contiene los criterios de búsqueda.
   * @return Una página de Entidades que cumplen con los criterios de búsqueda.
   */
  public Page<E> searchEntity(@NotNull Pageable page, boolean unpaged, String search) {
    BaseFilterBuilder<D, F> filterBuilder = instanceFilterBuilder(page, search);
    return unpaged
        ? searchEntityUnpaged(filterBuilder)
        : repository.findAll(createSpecificationField(filterBuilder), filterBuilder.getPage());
  }

  /**
   * Realiza una búsqueda no paginada y devuelve los resultados.
   *
   * @param filterBuilder El constructor de filtros que contiene los criterios de búsqueda.
   * @return Una página de Entidades que cumplen con los criterios de búsqueda.
   */
  private Page<E> searchEntityUnpaged(BaseFilterBuilder<D, F> filterBuilder) {
    List<E> result =
        repository
            .findAll(createSpecificationField(filterBuilder), filterBuilder.getPage().getSort())
            .stream()
            .toList();
    return new PageImpl<>(result, Pageable.unpaged(), result.size());
  }

  /**
   * Crea una especificación de búsqueda en base a los filtros proporcionados.
   *
   * @param filterBuilder El constructor de filtros que contiene los criterios de búsqueda.
   * @return Una especificación que puede ser usada para realizar una búsqueda en el repositorio.
   */
  public Specification<E> createSpecificationField(BaseFilterBuilder<D, F> filterBuilder) {
    List<Specification<E>> conditions = new ArrayList<>();

    if (ObjectUtils.isEmpty(filterBuilder.getFilters())) {
      return null;
    }

    // Se recorren los filtros y se añaden las especificaciones correspondientes
    for (SearchCriteria param : filterBuilder.getFilters()) {
      if (ReflectionUtil.getFields(ReflectionUtil.getParameterizedTypeClass(this.getClass(), 1))
          .stream()
          .noneMatch(f -> param.getKey().startsWith(f.getName()))) {
        throw new BadRequestException(ExceptionConstants.ERROR_INVALID_FILTER, param.getKey());
      }

      if (!ObjectUtils.isEmpty(param.getValue())) {
        // Si la clave contiene un punto y la especificación es nula, se crea una especificación
        if (param.getKey().contains(".") && param.getSpecification() == null) {
          Specification<E> specification = getSpecification(param);
          conditions.add(specification);
        }
        // Si la clave no contiene un punto y la especificación no es nula, se añade la
        // especificación
        else if (param.getSpecification() != null) {
          @SuppressWarnings("unchecked")
          Specification<E> specification = (Specification<E>) param.getSpecification();
          conditions.add(specification);
        }
        // Si la clave no contiene un punto y la especificación es nula, se añade un filtro
        else {
          conditions.add(new FilterSpecification<>(param));
        }
      }
    }

    return conditions.stream().reduce(Specification::and).orElse(null);
  }
}
