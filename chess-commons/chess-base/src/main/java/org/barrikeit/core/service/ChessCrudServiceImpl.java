package org.barrikeit.core.service;

import jakarta.persistence.criteria.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.barrikeit.core.domain.BaseEntity;
import org.barrikeit.core.error.BadRequestException;
import org.barrikeit.core.repository.ChessCrudRepository;
import org.barrikeit.core.rest.mapper.BaseMapper;
import org.barrikeit.core.rest.dto.BaseDto;
import org.barrikeit.core.service.excel.ExcelService;
import org.barrikeit.core.util.filter.FilterSpecification;
import org.barrikeit.core.util.filter.BaseFilter;
import org.barrikeit.core.util.filter.BaseFilterBuilder;
import org.barrikeit.core.util.ReflectionUtil;
import org.barrikeit.core.util.constants.ErrorConstants;
import org.barrikeit.core.util.search.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

/**
 * Esta es la clase LbdaCrudServiceImpl que extiende de LbdaBaseServiceImpl e implementa
 * LbdaCrudService. Esta clase proporciona la implementación de los métodos CRUD para las entidades
 * Lbda.
 *
 * <p>Parámetros de tipo: T - Representa la entidad que se va a manejar en el servicio. K -
 * Representa el tipo de la clave primaria de la entidad. D - Representa el DTO asociado a la
 * entidad. F - Representa el filtro base que se utilizará para las operaciones de búsqueda.
 *
 * <p>Campos: DTO_CLASS_INDEX - Índice de la clase DTO en la jerarquía de clases. repository -
 * Repositorio para realizar operaciones CRUD en la entidad. mapper - Mapper para convertir entre la
 * entidad y su DTO correspondiente.
 */
public abstract class ChessCrudServiceImpl<
        T extends BaseEntity, K extends Serializable, D extends BaseDto, F extends BaseFilter>
    extends BaseServiceImpl<T, K, D> implements ChessCrudService<T, K, D, F> {
  private static final int ENTITY_CLASS_INDEX = 0;
  private final ChessCrudRepository<T, K> repository;
  private final BaseMapper<T, D> mapper;
  private final ExcelService excelService;

  protected ChessCrudServiceImpl(
      ChessCrudRepository<T, K> repository, BaseMapper<T, D> mapper, ExcelService excelService) {
    super(repository, mapper);
    this.repository = repository;
    this.mapper = mapper;
    this.excelService = excelService;
  }

  /**
   * Realiza una búsqueda paginada o no paginada en base a los criterios de búsqueda proporcionados.
   *
   * @param page El objeto Pageable que contiene la información de paginación.
   * @param unpaged Un booleano que indica si la búsqueda debe ser paginada o no.
   * @param search Una cadena de texto que contiene los criterios de búsqueda.
   * @return Una página de DTOs que cumplen con los criterios de búsqueda.
   */
  public Page<D> search(@NotNull Pageable page, boolean unpaged, String search) {
    Page<T> paged = searchEntity(page, unpaged, search);
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
  public Page<T> searchEntity(@NotNull Pageable page, boolean unpaged, String search) {
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
  private Page<T> searchEntityUnpaged(BaseFilterBuilder<D, F> filterBuilder) {
    List<T> result =
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
  public Specification<T> createSpecificationField(BaseFilterBuilder<D, F> filterBuilder) {
    List<Specification<T>> conditions = new ArrayList<>();

    if (ObjectUtils.isEmpty(filterBuilder.getFilters())) {
      return null;
    }

    // Se recorren los filtros y se añaden las especificaciones correspondientes
    for (SearchCriteria param : filterBuilder.getFilters()) {
      if (Arrays.stream(
              ReflectionUtil.getAllFields(
                  ReflectionUtil.getClass(this.getClass(), ENTITY_CLASS_INDEX)))
          .noneMatch(f -> param.getKey().startsWith(f.getName()))) {
        throw new BadRequestException(ErrorConstants.ERROR_INVALID_FILTER, param.getKey());
      }

      if (!ObjectUtils.isEmpty(param.getValue())) {
        // Si la clave contiene un punto y la especificación es nula, se crea una especificación
        if (param.getKey().contains(".") && param.getSpecification() == null) {
          Specification<T> specification = getSpecification(param);
          conditions.add(specification);
        }
        // Si la clave no contiene un punto y la especificación no es nula, se añade la
        // especificación
        else if (param.getSpecification() != null) {
          @SuppressWarnings("unchecked")
          Specification<T> specification = (Specification<T>) param.getSpecification();
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

  private static <T extends BaseEntity> Specification<T> getSpecification(SearchCriteria param) {
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

  public <E extends BaseDto> void download(
      HttpServletResponse response,
      Pageable page,
      boolean unpaged,
      String search,
      BaseMapper<T, E> mapperDownload) {
    List<T> list = searchEntity(page, unpaged, search).getContent();
    if (!list.isEmpty()) {
      List<E> listDto = list.stream().map(mapperDownload::toDto).toList();
      excelService.downloadXlsx(response, listDto.get(0).getClass(), listDto);
    }
  }
}
