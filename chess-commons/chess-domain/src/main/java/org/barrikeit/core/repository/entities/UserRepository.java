package org.barrikeit.core.repository.entities;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.barrikeit.core.domain.entities.User;
import org.barrikeit.core.repository.ChessCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ChessCrudRepository<User, UUID> {

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

  List<User> findAllByEmailEqualsIgnoreCaseAndUsernameNotIgnoreCase(String email, String username);
}
