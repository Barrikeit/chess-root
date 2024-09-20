package org.barrikeit.core.service;

import java.io.Serializable;
import org.barrikeit.core.domain.ChessEntity;
import org.barrikeit.core.rest.dto.ChessDto;

public interface ChessEntityService<
        T extends ChessEntity<S>,
        K extends Serializable,
        D extends ChessDto<S>,
        S extends Serializable>
    extends BaseService<T, K, D> {
  D findByCode(S code);

  T findEntityByCode(S code);

  D update(S code, D dto);
}
