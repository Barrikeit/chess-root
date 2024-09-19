package org.barrikeit.core.repository.catalogue;

import org.barrikeit.core.domain.catalogue.GameState;
import org.barrikeit.core.repository.ChessEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameStateRepository extends ChessEntityRepository<GameState, Long, String> {}
