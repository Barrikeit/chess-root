package org.barrikeit.chess.domain.repository;

import org.barrikeit.chess.domain.model.Rank;
import org.barrikeit.chess.domain.repository.base.GenericCodeRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RankRepository extends GenericCodeRepository<Rank, Long, String> {}
