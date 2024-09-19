package org.barrikeit.core.repository.catalogue;

import org.barrikeit.core.domain.catalogue.Color;
import org.barrikeit.core.repository.ChessEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends ChessEntityRepository<Color, Long, String> {}
