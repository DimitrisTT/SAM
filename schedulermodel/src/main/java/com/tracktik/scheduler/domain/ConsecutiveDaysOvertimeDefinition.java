package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Accessors(chain = true)
@Data
public class ConsecutiveDaysOvertimeDefinition {
  private String id;
  private String name;
  private Long minimumDay;
  private Long maximumDay;
  private Long minimumHours;
  private Long maximumHours;
  private String overtimeType;

  public Long overtimeHours(List<Shift> shifts) {
    shifts.sort(Comparator.comparing(Shift::getStart));
    Long hours =  shifts.stream().skip(minimumDay).limit(maximumDay - minimumDay)
        .map(Shift::durationHours).mapToLong(l -> l).sum();

    if (hours < (minimumHours * 100)) return 0L;

    Long multiplier = overtimeType.equals("OT") ? 150L : 200L;

    Long hoursLessMinimum = hours - (minimumHours * 100);

    if (maximumHours == Long.MAX_VALUE) {
      return (hoursLessMinimum * multiplier) / 100;
    }
    Long adjustedHours = hoursLessMinimum - ((maximumHours - minimumHours) * 100);
    return adjustedHours * multiplier / 100;
  }

}
