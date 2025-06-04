package org.barrikeit.chess.domain.repository;

import org.barrikeit.chess.domain.entities.Friendship;
import org.barrikeit.chess.domain.repository.base.GenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRepository extends GenericRepository<Friendship, Long> {}
