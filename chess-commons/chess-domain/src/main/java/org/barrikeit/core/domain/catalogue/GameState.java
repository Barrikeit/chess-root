package org.barrikeit.core.domain.catalogue;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.barrikeit.core.domain.ChessEntity;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "game_states")
@AttributeOverride(name = "id", column = @Column(name = "id_state", nullable = false))
@AttributeOverride(name = "code", column = @Column(name = "code_state", nullable = false))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class GameState extends ChessEntity<String> {

  @Serial private static final long serialVersionUID = -5L;

  @Size(max = 255)
  @NotNull
  @Column(name = "state", nullable = false)
  private String state;

  @Size(max = 255)
  @NotNull
  @Column(name = "observations", nullable = false)
  private String observations;
}
