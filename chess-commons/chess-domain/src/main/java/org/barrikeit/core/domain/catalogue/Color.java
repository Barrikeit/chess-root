package org.barrikeit.core.domain.catalogue;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.barrikeit.core.domain.ChessEntity;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "colors")
@AttributeOverride(name = "id", column = @Column(name = "id_color", nullable = false))
@AttributeOverride(
    name = "code",
    column = @Column(name = "code_color", nullable = false, length = 1))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Color extends ChessEntity<String> {

  @Serial private static final long serialVersionUID = -3L;

  @Size(max = 5)
  @NotNull
  @Column(name = "color", nullable = false, length = 5)
  private String color;
}
