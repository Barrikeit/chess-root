package org.barrikeit.chess.domain.repository;

import org.barrikeit.chess.domain.model.Role;
import org.barrikeit.chess.domain.repository.base.GenericCodeRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends GenericCodeRepository<Role, Integer, String> {}
