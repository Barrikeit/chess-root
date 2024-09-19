package org.barrikeit.core.repository;

import java.io.Serializable;
import org.barrikeit.core.domain.BaseEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ChessCrudRepository<T extends BaseEntity, K extends Serializable>
    extends BaseRepository<T, K>, JpaSpecificationExecutor<T> {}
