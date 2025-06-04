package org.barrikeit.chess.core.service;

import java.io.Serializable;
import org.barrikeit.chess.core.service.dto.GenericDto;
import org.barrikeit.chess.core.service.excel.ExcelService;
import org.barrikeit.chess.core.service.mapper.GenericMapper;
import org.barrikeit.chess.core.util.filter.BaseFilter;
import org.barrikeit.chess.domain.entities.base.GenericEntity;
import org.barrikeit.chess.domain.repository.base.GenericRepository;

public abstract class GenericFilterService<
        E extends GenericEntity<I>,
        I extends Serializable,
        D extends GenericDto,
        F extends BaseFilter>
    extends GenericBaseFilterService<E, I, D, F, GenericRepository<E, I>, GenericMapper<E, D>> {

  protected GenericFilterService(
      GenericRepository<E, I> repository, GenericMapper<E, D> mapper, ExcelService excelService) {
    super(repository, mapper, excelService);
  }
}
