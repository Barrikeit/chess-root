package org.barrikeit.core.repository.relationships;

import org.barrikeit.core.domain.relationships.Friendship;
import org.barrikeit.core.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRepository extends BaseRepository<Friendship, Long> {}
