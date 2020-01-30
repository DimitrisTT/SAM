package com.tracktik.scheduler.api.domain;

import lombok.ToString;

/**
 * This is a class to marshall the fields linking employees to sites
 *
 * Methods imported by lombok:
 * toString
 */
@ToString
public class RequestEmployeeSiteAssignment {

  public String user_id;
  public String site_id;

}
