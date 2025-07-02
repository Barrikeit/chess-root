package org.barrikeit.chess.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.*;
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
@Table(name = EntityConstants.USERS)
@AttributeOverride(
    name = EntityConstants.ID,
    column = @Column(name = EntityConstants.ID_USER, nullable = false))
@AttributeOverride(
    name = EntityConstants.CODE,
    column = @Column(name = EntityConstants.CODE_USER, nullable = false))
public class User extends GenericCodeEntity<Long, UUID> {
  @Serial private static final long serialVersionUID = 1L;

  @NotNull
  @Size(max = 50)
  @Column(name = "username", nullable = false, length = 50, unique = true)
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
  @Size(max = 100)
  @Column(name = "email", nullable = false, length = 100, unique = true)
  private String email;

  @Size(max = 50)
  @Column(name = "phone", length = 50)
  private String phone;

  @NotNull
  @Size(max = 255)
  @Column(name = "password", nullable = false)
  private String password;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_location", referencedColumnName = "id_location")
  private Location location;

  @Column(name = "elo")
  private Integer elo;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_rank", referencedColumnName = "id_rank")
  private Rank rank;

  @NotNull
  @Column(name = "registered", nullable = false)
  private boolean registered = false;

  @Size(max = 255)
  @Column(name = "registration_code")
  private String registrationCode;

  @Column(name = "registration_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private LocalDateTime registrationDate;

  @Column(name = "enabled", nullable = false)
  private boolean enabled = false;

  @NotNull
  @Column(name = "banned", nullable = false)
  private boolean banned = false;

  @Column(name = "ban_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private LocalDateTime banDate;

  @Size(max = 255)
  @Column(name = "ban_reason")
  private String banReason;

  @NotNull
  @Column(name = "logged", nullable = false)
  private boolean logged = false;

  @Column(name = "login_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private LocalDateTime loginDate;

  @NotNull
  @Column(name = "login_attempts", nullable = false)
  private Integer loginAttempts = 0;

  @ManyToMany
  @JoinTable(
      name = "user_roles",
      joinColumns = @JoinColumn(name = "id_user"),
      inverseJoinColumns = @JoinColumn(name = "id_role"))
  private Set<Role> roles = new LinkedHashSet<>();

  @OneToMany(
      mappedBy = "user1",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private Set<Friendship> friendshipsInitiated;

  @OneToMany(
      mappedBy = "user2",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private Set<Friendship> friendshipsReceived;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof User e)) return false;
    if (!super.equals(o)) return false;

    return Objects.equals(code, e.code) && Objects.equals(name, e.name);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (code != null ? code.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "User{" + "code='" + code + '\'' + '}';
  }
}
