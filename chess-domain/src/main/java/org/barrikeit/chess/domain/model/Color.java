package org.barrikeit.chess.domain.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.barrikeit.chess.domain.model.base.GenericCodeEntity;
import org.barrikeit.chess.domain.util.constants.EntityConstants;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = EntityConstants.COLORS)
@AttributeOverride(
    name = EntityConstants.ID,
    column = @Column(name = EntityConstants.ID_COLOR, nullable = false))
@AttributeOverride(
    name = EntityConstants.CODE,
    column = @Column(name = EntityConstants.CODE_COLOR, nullable = false, length = 1))
public class Color extends GenericCodeEntity<Long, String> {
  @Serial private static final long serialVersionUID = 1L;

  @Size(max = 5)
  @NotNull
  @Column(name = "color", nullable = false, length = 5)
  private String name;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Color e)) return false;
    if (!super.equals(o)) return false;

    return Objects.equals(code, e.code) && Objects.equals(name, e.name);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (code != null ? code.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Color{" + "code='" + code + '\'' + '}';
  }
}
