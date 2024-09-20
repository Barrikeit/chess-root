package org.barrikeit.core.util;

import static org.barrikeit.core.util.constants.ApplicationConstants.PATTERN_DATE_TIME;
import static org.barrikeit.core.util.constants.ApplicationConstants.PATTERN_LOCAL_DATE;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import org.barrikeit.core.error.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TimeUtil {

  private static String zone;

  @Value("${chess.application.timeZone:Europe/Madrid}")
  public void setZoneStatic(String zone) {
    TimeUtil.zone = zone;
  }

  public static Instant nowInstant() {
    return Instant.now().atZone(ZoneId.of(zone)).toInstant();
  }

  public static Date nowDate() {
    return Date.from(nowInstant());
  }

  public static LocalDate nowLocalDate() {
    return nowInstant().atZone(ZoneId.of(zone)).toLocalDate();
  }

  public static LocalDateTime nowLocalDateTime() {
    return nowInstant().atZone(ZoneId.of(zone)).toLocalDateTime();
  }

  public static LocalDate convertLocalDate(String date) {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(PATTERN_LOCAL_DATE);

    try {
      return LocalDate.parse(date, dateFormatter);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Formato de fecha y hora inválido: " + date);
    }
  }

  public static LocalDateTime convertLocalDateTime(String date) {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(PATTERN_LOCAL_DATE);
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN_DATE_TIME);

    try {
      return LocalDateTime.parse(date, dateTimeFormatter);
    } catch (DateTimeParseException e) {
      try {
        return LocalDateTime.of(LocalDate.parse(date, dateFormatter), LocalTime.MIN);
      } catch (DateTimeParseException ex) {
        throw new BadRequestException("Formato de fecha y hora inválido: " + date);
      }
    }
  }

  public static String formatLocalDateTime(LocalDateTime date, String format) {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
    return date.format(dateTimeFormatter);
  }

  public static String formatLocalDate(LocalDate date, String format) {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
    return date.format(dateFormatter);
  }
}
