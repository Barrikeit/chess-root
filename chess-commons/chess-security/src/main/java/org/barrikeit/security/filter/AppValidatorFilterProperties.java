package org.barrikeit.security.filter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(
    prefix = "chess.authorization.app-validator-filter",
    ignoreUnknownFields = true)
public class AppValidatorFilterProperties {

  private Boolean appHeaderNameValidationFilter;

  private String appHeaderName;

  private String appSelfName;

  private String appSecurityName;

  private String appCatalogoName;

  private String appSueName;

  private String appDoctemplateName;

  private String appFlujoFirmaName;

  private String appConvocatoriaName;

  private String appDocrepoName;

  private String appTramitacionName;

  private String appContafiscalizaName;

  private String appComunicacionName;

  private String appSolicitudName;

  private String appGestiecoName;

  private String appExpedienteName;
}
