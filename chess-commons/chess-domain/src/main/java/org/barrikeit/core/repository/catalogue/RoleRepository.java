package org.barrikeit.core.repository.catalogue;

import org.barrikeit.core.domain.catalogue.Role;
import org.barrikeit.core.repository.ChessEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends ChessEntityRepository<Role, Long, String> {}
