package org.barrikeit.core.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class UserExtra {
  @Column(name = "registered", nullable = false)
  private boolean registered = false;

  @Column(name = "registration_code")
  private String registrationCode;

  @Column(name = "registration_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private LocalDateTime registrationDate;

  @Column(name = "logged", nullable = false)
  private boolean logged = false;

  @Column(name = "login_attempts")
  private int loginAttempts;

  @Column(name = "login_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private LocalDateTime loginDate;

  @Column(name = "banned", nullable = false)
  private boolean banned = false;

  @Column(name = "ban_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private LocalDateTime banDate;

  @Column(name = "ban_reason")
  private String banReason;
}
