package org.barrikeit.core.repository.entities;

import java.util.UUID;
import org.barrikeit.core.domain.entities.Game;
import org.barrikeit.core.repository.ChessCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends ChessCrudRepository<Game, UUID> {}
