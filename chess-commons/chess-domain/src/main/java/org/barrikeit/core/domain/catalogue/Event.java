package org.barrikeit.core.domain.catalogue;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.barrikeit.core.domain.ChessEntity;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "events")
@AttributeOverride(name = "id", column = @Column(name = "id_event", nullable = false))
@AttributeOverride(name = "code", column = @Column(name = "code_event", nullable = false))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Event extends ChessEntity<UUID> {

  @Serial private static final long serialVersionUID = -4L;

  @Size(max = 255)
  @NotNull
  @Column(name = "name", nullable = false)
  private String name;

  @NotNull
  @Column(name = "start_date", nullable = false, columnDefinition = "DATE")
  private LocalDate startDate;

  @Column(name = "start_date", columnDefinition = "DATE")
  private LocalDate endDate;

  @Column(name = "round", columnDefinition = "DATE")
  private int round;

  @ManyToOne(fetch = FetchType.LAZY)
  private Location location;
}
