package com.vanderlei.cfp.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {

  public static boolean isValid(final String date) {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
      LocalDate dateParse = LocalDate.parse(date, formatter);
      return true;
    } catch (DateTimeParseException e) {
      return false;
    }
  }

  public static LocalDate getValidDate(final int day, final int month, final int year) {
    return LocalDate.of(year, month, day);
  }
}
