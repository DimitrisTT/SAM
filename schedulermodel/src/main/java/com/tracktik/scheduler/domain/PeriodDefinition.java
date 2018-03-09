package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class PeriodDefinition {

  private Integer startDay;
  private Integer numberOfDays;
}
