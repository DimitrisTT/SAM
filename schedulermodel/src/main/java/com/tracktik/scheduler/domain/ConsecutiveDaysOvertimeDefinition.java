package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class ConsecutiveDaysOvertimeDefinition {

  private Integer minimumDays;
  private Integer maximumDays;
  private Integer minimumHours;
  private Integer maximumHours;
  private Float multiplier;
}
