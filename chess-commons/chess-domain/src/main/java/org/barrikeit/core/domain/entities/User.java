package org.barrikeit.core.domain.entities;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.barrikeit.core.domain.ChessEntity;
import org.barrikeit.core.domain.catalogue.Location;
import org.barrikeit.core.domain.catalogue.Rank;
import org.barrikeit.core.domain.catalogue.Role;
import org.barrikeit.core.domain.relationships.Friendship;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name = "users")
@AttributeOverride(name = "id", column = @Column(name = "id_user", nullable = false))
@AttributeOverride(name = "code", column = @Column(name = "code_user", nullable = false))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class User extends ChessEntity<UUID> {

  @Serial private static final long serialVersionUID = -1L;

  @NotNull
  @Size(max = 50)
  @Column(name = "username", nullable = false, length = 50)
  private String username;

  @NotNull
  @Size(max = 50)
  @Column(name = "name", nullable = false, length = 50)
  private String name;

  @NotNull
  @Size(max = 50)
  @Column(name = "surname1", nullable = false, length = 50)
  private String surname1;

  @Size(max = 50)
  @Column(name = "surname2", length = 50)
  private String surname2;

  @NotNull
  @Size(max = 255)
  @Column(name = "email", nullable = false)
  private String email;

  @NotNull
  @Size(max = 50)
  @Column(name = "phone", nullable = false, length = 50)
  private String phone;

  @NotNull
  @Size(max = 255)
  @Column(name = "password", nullable = false)
  private String password;

  @NotNull
  @NotAudited
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_location", referencedColumnName = "id_location")
  private Location location;

  @Size(max = 255)
  @Column(name = "elo", nullable = false)
  private String elo;

  @NotNull
  @NotAudited
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_rank", referencedColumnName = "id_rank")
  private Rank rank;

  @Embedded private UserExtra userExtra;

  @NotAudited
  @ManyToMany
  @JoinTable(
      name = "users_roles",
      joinColumns = @JoinColumn(name = "id_user"),
      inverseJoinColumns = @JoinColumn(name = "id_role"))
  private Set<Role> roles = new LinkedHashSet<>();

  @NotAudited
  @OneToMany(mappedBy = "user1", fetch = FetchType.LAZY)
  private Set<Friendship> friendshipsInitiated = new HashSet<>();

  @NotAudited
  @OneToMany(mappedBy = "user2", fetch = FetchType.LAZY)
  private Set<Friendship> friendshipsReceived = new HashSet<>();

  @Transient private Set<Friendship> friendships = new HashSet<>();
  @Transient private Set<User> friends = new HashSet<>();

  public Set<Friendship> getFriendships() {
    if (!friendshipsInitiated.isEmpty() || !friendshipsReceived.isEmpty()) {
      friendships.clear();
      friendships.addAll(friendshipsInitiated);
      friendships.addAll(friendshipsReceived);
    }
    return friendships;
  }

  public Set<User> getFriends(User currentUser, Set<Friendship> friendships) {
    friends.clear();
    for (Friendship friendship : getFriendships()) {
      friends.add(friendship.getFriend(currentUser));
    }
    return friends;
  }
}
