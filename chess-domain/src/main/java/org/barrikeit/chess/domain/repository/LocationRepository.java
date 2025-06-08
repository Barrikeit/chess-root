package org.barrikeit.chess.domain.repository;

import java.util.UUID;
import org.barrikeit.chess.domain.model.Location;
import org.barrikeit.chess.domain.repository.base.GenericCodeRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends GenericCodeRepository<Location, Long, UUID> {}
