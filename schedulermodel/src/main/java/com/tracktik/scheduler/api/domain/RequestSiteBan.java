package com.tracktik.scheduler.api.domain;

import lombok.ToString;

/**
 * This is a class to marshall the fields linking Employees to sites they're banned from
 *
 * Methods imported by lombok:
 * toString
 */
@ToString
public class RequestSiteBan {
  public String site_id;
  public String employee_id;

}
