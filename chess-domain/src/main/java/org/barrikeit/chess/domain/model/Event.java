package org.barrikeit.chess.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.time.LocalDate;
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
@Table(name = EntityConstants.EVENTS)
@AttributeOverride(
    name = EntityConstants.ID,
    column = @Column(name = EntityConstants.ID_EVENT, nullable = false))
@AttributeOverride(
    name = EntityConstants.CODE,
    column = @Column(name = EntityConstants.CODE_EVENT, nullable = false))
public class Event extends GenericCodeEntity<Long, UUID> {
  @Serial private static final long serialVersionUID = 1L;

  @Size(max = 255)
  @NotNull
  @Column(name = "event", nullable = false)
  private String name;

  @NotNull
  @Column(name = "start_date", nullable = false, columnDefinition = "DATE")
  private LocalDate startDate;

  @Column(name = "end_date", columnDefinition = "DATE")
  private LocalDate endDate;

  @NotNull
  @Column(name = "round", nullable = false)
  private Integer round;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "id_location", referencedColumnName = "id_location", nullable = false)
  private Location location;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Event e)) return false;
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
    return "Event{" + "code='" + code + '\'' + '}';
  }
}
