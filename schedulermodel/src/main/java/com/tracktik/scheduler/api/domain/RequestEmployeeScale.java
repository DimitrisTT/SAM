package com.tracktik.scheduler.api.domain;

import lombok.ToString;
/**
 * This is a class to marshall the fields linking Employees to their Scales
 *
 * Methods imported by lombok:
 * toString
 */
@ToString
public class RequestEmployeeScale {

  public String scale_id;
  public String employee_id;

}
