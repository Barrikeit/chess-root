package org.barrikeit.chess.domain.repository;

import org.barrikeit.chess.domain.model.GameUserColor;
import org.barrikeit.chess.domain.repository.base.GenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameUserColorRepository extends GenericRepository<GameUserColor, Long> {}
