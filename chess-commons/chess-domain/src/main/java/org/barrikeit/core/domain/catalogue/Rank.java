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
@Table(name = "ranks")
@AttributeOverride(name = "id", column = @Column(name = "id_rank", nullable = false))
@AttributeOverride(
    name = "code",
    column = @Column(name = "code_rank", nullable = false, length = 2))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Rank extends ChessEntity<String> {

  @Serial private static final long serialVersionUID = -8L;

  @NotNull
  @Size(max = 255)
  @Column(name = "rank", nullable = false)
  private String rank;
}
