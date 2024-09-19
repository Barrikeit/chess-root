package org.barrikeit.core.repository.catalogue;

import org.barrikeit.core.domain.catalogue.Location;
import org.barrikeit.core.repository.ChessEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends ChessEntityRepository<Location, Long, String> {}
