package com.tracktik.scheduler.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.threeten.extra.Interval;

import java.time.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Accessors(chain = true)
@EqualsAndHashCode(of = "id")
@Data
public class PayrollSchedule {
  private String id;
  private String name;
  private String frequency;
  @JsonDeserialize(using = LocalTimeDeserializer.class)
  @JsonSerialize(using = LocalTimeSerializer.class)
  private LocalTime periodStartTime;
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonSerialize(using = LocalDateSerializer.class)
  private LocalDate periodStartDate;
  private OverlappingMethodType overlappingMethod;
  private Boolean countHolidayHoursTowardsPeriodOvertime = false;
  private Boolean alignHolidaysWithPeriodStartTime = false;

  public LocalDate getPeriodEndDate() {
    if (frequency.equals("bi_weekly")) {
      return periodStartDate.plusDays(14);
    }
    if (frequency.equals("weekly")) {
      return periodStartDate.plusDays(7);
    }
    throw new RuntimeException("Unsupported frequency: " + frequency);
  }

  public DayOfWeek getPeriodStartDay() {
    return periodStartDate.getDayOfWeek();
  }

  public Set<PayPeriod> enclosingPeriods(LocalDateTime startTime, LocalDateTime endTime) {

    Set<PayPeriod> periods = new HashSet<>();
    Duration duration = Duration.ofDays(frequency.equals("weekly") ? 7 : 14);

    Interval totalInterval = Interval.of(startTime.atZone(ZoneId.systemDefault()).toInstant(), endTime.atZone(ZoneId.systemDefault()).toInstant());

    LocalDateTime periodStart = LocalDateTime.of(periodStartDate, periodStartTime);
    LocalDateTime periodEnd = periodStart.plus(duration);

    while (!periodStart.isAfter(endTime)) {
      Interval payInterval = Interval.of(periodStart.atZone(ZoneId.systemDefault()).toInstant(), periodEnd.atZone(ZoneId.systemDefault()).toInstant());
      if (payInterval.overlaps(totalInterval)) {
        periods.add(new PayPeriod().setStart(periodStart).setEnd(periodEnd));
      }
      periodStart = periodStart.plus(duration);
      periodEnd = periodEnd.plus(duration);
    }

    return periods;
  }
}
