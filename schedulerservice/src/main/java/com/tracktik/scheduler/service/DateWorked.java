package com.tracktik.scheduler.service;

import lombok.Data;
import lombok.experimental.Accessors;
import org.kie.api.definition.type.PropertyReactive;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class DateWorked {
  private String employeeId;
  private LocalDate date;

  public static Boolean datesAreConsecutive(List<DateWorked> dates) {


    List<LocalDate> sortedList = dates.stream()
        .map(DateWorked::getDate)
        .sorted().collect(Collectors.toList());

    LocalDate previous = null;
    for (LocalDate date : sortedList) {
      if (previous != null) {
        if (!previous.plusDays(1).equals(date)) return false;
      }
      previous = date;
    }
    return true;

  }
}
