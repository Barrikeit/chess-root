package org.barrikeit.core.repository.catalogue;

import org.barrikeit.core.domain.catalogue.Rank;
import org.barrikeit.core.repository.ChessEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RankRepository extends ChessEntityRepository<Rank, Long, String> {}
