package com.tracktik.scheduler.api.domain;

import lombok.ToString;

/**
 * This is a class to marshall the fields related to Employee Availability
 *
 * Methods imported by lombok:
 * toString
 */
@ToString
public class RequestEmployeeAvailability {

  public String employee_id;
  public String type;
  public String day_of_week;
  public String seconds_start;
  public String seconds_end;

}
