package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@EqualsAndHashCode(of = "id")
@Data
public class PeriodOvertimeDefinition {
  private String id;
  private String name;
  private Long minimumHours;  //should be multiplied by 100 to avoid floating point math
  private Long maximumHours;  //should be multiplied by 100 to avoid floating point math
  private String overtimeType;
}
