package org.barrikeit.security.service;

import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;

@Service
public class SpringSessionServiceImpl<S extends Session> implements SpringSessionService {

  private final FindByIndexNameSessionRepository<S> sessionRepository;

  public SpringSessionServiceImpl(FindByIndexNameSessionRepository<S> sessionRepository) {
    this.sessionRepository = sessionRepository;
  }

  /**
   * Devuelve si existe un registro en la tabla de sesiones de Spring identificado por el valor
   * unico sessionId, es equivalente a devolver si existe una HttpSesion activa con ese sessionId
   *
   * @param sessionId Identificador de la Sesion
   * @return boolean indicando si existe un registro
   */
  @Override
  public boolean existeSesionBySessionId(String sessionId) {
    return sessionRepository.findById(sessionId) != null;
  }
}
