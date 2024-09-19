package org.barrikeit.core.repository;

import java.io.Serializable;
import org.barrikeit.core.domain.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, K extends Serializable>
    extends JpaRepository<T, K> {}
