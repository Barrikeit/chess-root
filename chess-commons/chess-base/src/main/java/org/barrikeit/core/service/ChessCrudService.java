package org.barrikeit.core.service;

import jakarta.servlet.http.HttpServletResponse;
import java.io.Serializable;
import org.barrikeit.core.domain.BaseEntity;
import org.barrikeit.core.rest.mapper.BaseMapper;
import org.barrikeit.core.rest.dto.BaseDto;
import org.barrikeit.core.util.filter.BaseFilter;
import org.barrikeit.core.util.filter.BaseFilterBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChessCrudService<
        T extends BaseEntity, K extends Serializable, D extends BaseDto, F extends BaseFilter>
    extends BaseService<T, K, D> {

  BaseFilterBuilder<D, F> instanceFilterBuilder(Pageable page, String search);

  Page<D> search(Pageable page, boolean unpaged, String search);

  Page<T> searchEntity(Pageable page, boolean unpaged, String search);

  <E extends BaseDto> void download(
      HttpServletResponse response,
      Pageable page,
      boolean unpaged,
      String search,
      BaseMapper<T, E> mapperDownload);
}
