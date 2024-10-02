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
@Table(name = "roles")
@AttributeOverride(name = "id", column = @Column(name = "id_role", nullable = false))
@AttributeOverride(
    name = "code",
    column = @Column(name = "code_role", nullable = false, length = 2))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Role extends ChessEntity<String> {

  @Serial private static final long serialVersionUID = -9L;

  @Size(max = 50)
  @NotNull
  @Column(name = "role", nullable = false, length = 50)
  private String role;
}
