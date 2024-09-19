package org.barrikeit.core.domain.relationships;

import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.barrikeit.core.domain.BaseEntity;
import org.barrikeit.core.domain.entities.User;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(
    name = "friendships",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "unique_friendships",
          columnNames = {"id_user1", "id_user2"})
    })
@AttributeOverride(name = "id", column = @Column(name = "id_friendship", nullable = false))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Friendship extends BaseEntity implements Serializable {

  @Serial private static final long serialVersionUID = -10L;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_user1", referencedColumnName = "id_user")
  private User user1;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_user2", referencedColumnName = "id_user")
  private User user2;

  @Column(name = "status", nullable = false)
  private String status;

  @Transient private User friend;

  public User getFriend(User currentUser) {
    if (currentUser.equals(user1)) {
      return user2;
    } else {
      return user1;
    }
  }
}
