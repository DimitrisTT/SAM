package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class EmployeeConstraintMultiplier {

  private String employeeId;
  private String name;
  private Double multiplier = 1.0d;
  private Boolean alreadyFired = false;

}
