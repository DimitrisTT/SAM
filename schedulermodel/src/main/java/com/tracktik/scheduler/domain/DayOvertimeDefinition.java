package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class DayOvertimeDefinition {

  private Integer minimumHours;
  private Integer maximumHours;
  private Float multiplier;

}
