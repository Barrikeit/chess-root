package org.barrikeit.chess.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
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
@Table(name = EntityConstants.GAMES)
@AttributeOverride(
    name = EntityConstants.ID,
    column = @Column(name = EntityConstants.ID_GAME, nullable = false))
@AttributeOverride(
    name = EntityConstants.CODE,
    column = @Column(name = EntityConstants.CODE_GAME, nullable = false))
public class Game extends GenericCodeEntity<Long, UUID> {
  @Serial private static final long serialVersionUID = 1L;

  @NotNull
  @Column(name = "pgn", nullable = false)
  private String pgn;

  @NotNull
  @Column(name = "fen", nullable = false)
  private String fen;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "id_game_state", referencedColumnName = "id_game_state", nullable = false)
  private GameState gameState;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "id_event", referencedColumnName = "id_event", nullable = false)
  private Event event;

  @NotNull
  @Column(name = "start_date", columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
  private LocalDateTime startDate;

  @Column(name = "end_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private LocalDateTime endDate;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Game e)) return false;
    if (!super.equals(o)) return false;

    return Objects.equals(code, e.code) && Objects.equals(pgn, e.pgn);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (code != null ? code.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Game{" + "code='" + code + '\'' + '}';
  }
}
