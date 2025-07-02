package org.barrikeit.chess.domain.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Getter
@AllArgsConstructor
public enum FriendshipStatus {
  PENDING("Pending"),
  ACCEPTED("Pending"),
  BLOCKED("Blocked");

  public final String description;

  public static List<SelectOption> getOptions() {
    final ArrayList<SelectOption> options = new ArrayList<>();
    for (final FriendshipStatus option :
        Arrays.stream(FriendshipStatus.values())
            .sorted(Comparator.comparing(Enum::name))
            .toList()) {
      options.add(new SelectOption(option.getDescription(), option.name()));
    }
    return options;
  }
}
