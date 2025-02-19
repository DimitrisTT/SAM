package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.DayOfWeek;

@Accessors(chain = true)
@Data
public class PeriodDefinition {
  private DayOfWeek startDay;
  private Integer numberOfDays;
}
