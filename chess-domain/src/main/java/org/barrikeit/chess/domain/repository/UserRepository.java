package org.barrikeit.chess.domain.repository;

import java.util.Optional;
import java.util.UUID;
import org.barrikeit.chess.domain.model.User;
import org.barrikeit.chess.domain.repository.base.GenericCodeRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends GenericCodeRepository<User, Long, UUID> {

  Optional<User> findByUsernameEqualsIgnoreCase(String user);

  Optional<User> findByEmailEqualsIgnoreCase(String email);

  Optional<User> findByUsernameEqualsIgnoreCaseAndEmailEqualsIgnoreCase(String user, String email);
}
