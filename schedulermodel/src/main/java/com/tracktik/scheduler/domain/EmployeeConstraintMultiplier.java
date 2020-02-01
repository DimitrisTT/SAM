package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This is a class to hold the Employee Constraint Multiplier
 * related to an employee, this usually is added in at ruleTime and serves as a flag to prevent infinite rule firing
 *
 * Methods imported by lombok:
 * Data
 * Accessors
 */
@Accessors(chain = true)
@Data
public class EmployeeConstraintMultiplier {

  private String employeeId;
  private String name;
  private Double multiplier = 1.0d;
  private Boolean alreadyFired = false;

}
