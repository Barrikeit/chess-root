package org.barrikeit.core.domain;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.NotAudited;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@SuperBuilder(toBuilder = true)
public abstract class ChessEntity<S extends Serializable> extends BaseEntity {

  @Serial private static final long serialVersionUID = 1L;

  @NotNull @NotAudited protected S code;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ChessEntity<? extends Serializable> that)) return false;
    if (!super.equals(o)) return false;

    return Objects.equals(code, that.code);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (code != null ? code.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "ChessEntity{" + "code='" + code + '\'' + '}';
  }
}
