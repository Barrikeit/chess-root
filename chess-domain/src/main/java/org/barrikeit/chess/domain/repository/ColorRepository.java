package org.barrikeit.chess.domain.repository;

import org.barrikeit.chess.domain.model.Color;
import org.barrikeit.chess.domain.repository.base.GenericCodeRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends GenericCodeRepository<Color, Long, String> {}
