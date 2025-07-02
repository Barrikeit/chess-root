package org.barrikeit.chess.domain.repository;

import java.util.UUID;
import org.barrikeit.chess.domain.model.Game;
import org.barrikeit.chess.domain.repository.base.GenericCodeRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends GenericCodeRepository<Game, Long, UUID> {}
