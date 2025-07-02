package org.barrikeit.chess.core.util.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccessRow {
  private Object[] datos;
  private List<Integer> uniqueColumnsIndexes;

  public AccessRow(Object[] datos, List<Integer> uniqueColumnsIndex) {
    this.datos = datos;
    this.uniqueColumnsIndexes = uniqueColumnsIndex;
  }
}
