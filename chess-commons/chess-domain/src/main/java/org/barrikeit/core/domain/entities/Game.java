package org.barrikeit.core.domain.entities;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.barrikeit.core.domain.ChessEntity;
import org.barrikeit.core.domain.catalogue.GameState;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name = "games")
@AttributeOverride(name = "id", column = @Column(name = "id_game", nullable = false))
@AttributeOverride(name = "code", column = @Column(name = "code_game", nullable = false))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Game extends ChessEntity<UUID> {

  @Serial private static final long serialVersionUID = -2L;

  @NotNull
  @Column(name = "start_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private LocalDateTime startDate;

  @Column(name = "end_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private LocalDateTime endDate;

  @NotNull
  @NotAudited
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_state", referencedColumnName = "id_state")
  private GameState gameState;
}
