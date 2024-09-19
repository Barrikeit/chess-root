package org.barrikeit.core.repository.catalogue;

import org.barrikeit.core.domain.catalogue.Event;
import org.barrikeit.core.repository.ChessCrudRepository;
import org.barrikeit.core.repository.ChessEntityRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventRepository extends ChessCrudRepository<Event, UUID> {}
