package org.barrikeit.core.util;

import static org.barrikeit.core.util.constants.ApplicationConstants.DATE_FORMATTER;
import static org.barrikeit.core.util.constants.ApplicationConstants.DATE_TIME_FORMATTER;
import static org.barrikeit.core.util.constants.ApplicationConstants.DEFAULT_TIME_ZONE;
import static org.barrikeit.core.util.constants.ApplicationConstants.PATTERN_LOCAL_DATE;
import static org.barrikeit.core.util.constants.ApplicationConstants.PATTERN_LOCAL_DATE_TIME;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TimeUtil {
  private TimeUtil() {throw new IllegalStateException("Utility class");}

  @Value("${chess.application.timeZone:" + DEFAULT_TIME_ZONE + "}")
  private static ZoneId zone;

  public static Instant instantNow() {
    return Instant.now().atZone(zone).toInstant();
  }

  public static Date dateNow() {
    return Date.from(instantNow());
  }

  public static LocalDate localDateNow() {
    return instantNow().atZone(zone).toLocalDate();
  }

  public static LocalDateTime localDateTimeNow() {
    return instantNow().atZone(zone).toLocalDateTime();
  }

  public static LocalDate convertLocalDate(String date) {
    try {
      return LocalDate.parse(date, DATE_FORMATTER);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Invalid date format: " + date, e);
    }
  }

  public static LocalDateTime convertLocalDateTime(String date) {
    try {
      return LocalDateTime.parse(date, DATE_TIME_FORMATTER);
    } catch (DateTimeParseException e) {
      // Attempt to parse as LocalDate and combine with LocalTime.MIN
      try {
        return LocalDateTime.of(LocalDate.parse(date, DATE_FORMATTER), LocalTime.MIN);
      } catch (DateTimeParseException ex) {
        throw new IllegalArgumentException("Invalid date and time format: " + date, ex);
      }
    }
  }

  public static String formatLocalDateTime(LocalDateTime date, String format) {
    return date.format(DateTimeFormatter.ofPattern(format));
  }

  public static String formatLocalDate(LocalDate date, String format) {
    return date.format(DateTimeFormatter.ofPattern(format));
  }
}
