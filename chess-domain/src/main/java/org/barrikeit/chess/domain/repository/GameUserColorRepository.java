package org.barrikeit.chess.domain.repository;

import org.barrikeit.chess.domain.entities.GameUserColor;
import org.barrikeit.chess.domain.repository.base.GenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameUserColorRepository extends GenericRepository<GameUserColor, Long> {}
