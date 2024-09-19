package org.barrikeit.core.repository;

import java.io.Serializable;
import java.util.Optional;
import org.barrikeit.core.domain.ChessEntity;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ChessEntityRepository<
        T extends ChessEntity<S>, K extends Serializable, S extends Serializable>
    extends BaseRepository<T, K> {

  Optional<T> findByCode(S code);
}
