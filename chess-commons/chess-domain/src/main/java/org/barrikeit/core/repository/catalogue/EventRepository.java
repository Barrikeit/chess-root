package org.barrikeit.core.repository.catalogue;

import java.util.UUID;
import org.barrikeit.core.domain.catalogue.Event;
import org.barrikeit.core.repository.ChessCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends ChessCrudRepository<Event, UUID> {}
