package com.tracktik.scheduler.api.domain;

import lombok.ToString;

/**
 * This is a class to marshall the fields related to time off, and linking them to an employee
 *
 * Methods imported by lombok:
 * toString
 */
@ToString
public class RequestTimeOff {

  public String start_time;
  public String end_time;
  public String employee_id;

}
