package org.barrikeit.chess.core.util.search;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.barrikeit.chess.core.util.enums.SearchOperation;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;

/**
 * La clase SearchCriteria representa los criterios de búsqueda que se utilizarán para filtrar los
 * resultados de una consulta. Cada instancia de SearchCriteria contiene una clave, una operación y
 * un valor. También puede contener una especificación de JPA para realizar consultas más complejas.
 */
@Builder
@Getter
@Setter
public class SearchCriteria implements Serializable {

  /** La clave es el nombre del campo que se va a buscar. */
  private String key;

  /** La operación es la operación que se va a realizar, como: =, >, >, etc.. */
  private SearchOperation operation;

  /** El valor es el valor que se va a comparar con el valor del campo. */
  private Object value;

  /** Una especificación de JPA que se puede utilizar para realizar consultas más complejas. */
  private Specification<?> specification;
}
