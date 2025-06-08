package org.barrikeit.chess.domain.repository;

import java.util.UUID;
import org.barrikeit.chess.domain.model.Event;
import org.barrikeit.chess.domain.repository.base.GenericCodeRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends GenericCodeRepository<Event, Long, UUID> {}
