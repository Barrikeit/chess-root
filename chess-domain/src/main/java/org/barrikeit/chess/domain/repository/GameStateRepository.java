package org.barrikeit.chess.domain.repository;

import org.barrikeit.chess.domain.entities.GameState;
import org.barrikeit.chess.domain.repository.base.GenericCodeRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameStateRepository extends GenericCodeRepository<GameState, Long, String> {}
