package org.barrikeit.chess.domain.model;

import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.barrikeit.chess.domain.model.base.GenericEntity;
import org.barrikeit.chess.domain.util.constants.EntityConstants;
import org.barrikeit.chess.domain.util.enums.FriendshipStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(
    name = EntityConstants.FRIENDSHIPS,
    uniqueConstraints = {
      @UniqueConstraint(
          name = "unique_friendships",
          columnNames = {"id_user1", "id_user2"})
    })
@AttributeOverride(
    name = EntityConstants.ID,
    column = @Column(name = EntityConstants.ID_FRIENDSHIP, nullable = false))
public class Friendship extends GenericEntity<Long> implements Serializable {
  @Serial private static final long serialVersionUID = 1L;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_user1", referencedColumnName = "id_user")
  private User user1;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_user2", referencedColumnName = "id_user")
  private User user2;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private FriendshipStatus status = FriendshipStatus.PENDING;

  @Transient private User friend;

  public User getFriend(User currentUser) {
    if (currentUser.equals(user1)) {
      return user2;
    } else {
      return user1;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Friendship e)) return false;
    if (!super.equals(o)) return false;

    return Objects.equals(user1.getCode(), e.getUser1().getCode())
        && Objects.equals(user2.getCode(), e.getUser2().getCode())
        && Objects.equals(status, e.getStatus());
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "Friendship{" + "user1=" + user1 + ", user2=" + user2 + ", status=" + status + '}';
  }
}
