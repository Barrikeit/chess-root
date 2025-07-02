package org.barrikeit.chess.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.barrikeit.chess.domain.model.base.GenericEntity;
import org.barrikeit.chess.domain.util.constants.EntityConstants;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(
    name = EntityConstants.GAMES_USERS_COLORS,
    uniqueConstraints = {
      @UniqueConstraint(
          name = "unique_user_game_color",
          columnNames = {"id_user", "id_game", "id_color"}),
      @UniqueConstraint(
          name = "unique_game_color",
          columnNames = {"id_game", "id_color"})
    })
@AttributeOverride(
    name = EntityConstants.ID,
    column = @Column(name = EntityConstants.ID_GAME_USER_COLOR, nullable = false))
public class GameUserColor extends GenericEntity<Long> {
  @Serial private static final long serialVersionUID = 1L;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "id_game", referencedColumnName = "id_game", nullable = false)
  private Game game;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "id_user", referencedColumnName = "id_user", nullable = false)
  private User user;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "id_color", referencedColumnName = "id_color", nullable = false)
  private Color color;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof GameUserColor e)) return false;
    if (!super.equals(o)) return false;

    return Objects.equals(game.getCode(), e.getGame().getCode())
        && Objects.equals(user.getCode(), e.getUser().getCode())
        && Objects.equals(color.getCode(), e.getColor().getCode());
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "GameUserColor{" + "game=" + game + ", user=" + user + ", color=" + color + '}';
  }
}
