package org.barrikeit.core.domain.relationships;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.barrikeit.core.domain.BaseEntity;
import org.barrikeit.core.domain.catalogue.Color;
import org.barrikeit.core.domain.entities.Game;
import org.barrikeit.core.domain.entities.User;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(
    name = "games_users_colors",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "unique_user_game_color",
          columnNames = {"id_user", "id_game", "id_color"}),
      @UniqueConstraint(
          name = "unique_game_color",
          columnNames = {"id_game", "id_color"})
    })
@AttributeOverride(name = "id", column = @Column(name = "id_game_user_color", nullable = false))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class GameUserColor extends BaseEntity implements Serializable {

  @Serial private static final long serialVersionUID = -11L;

  @NotNull
  @NotAudited
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_game", referencedColumnName = "id_game")
  private Game game;

  @NotNull
  @NotAudited
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_user", referencedColumnName = "id_user")
  private User user;

  @NotNull
  @NotAudited
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_color", referencedColumnName = "id_color")
  private Color color;
}
