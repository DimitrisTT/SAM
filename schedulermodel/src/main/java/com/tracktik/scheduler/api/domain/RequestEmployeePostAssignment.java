package com.tracktik.scheduler.api.domain;

import lombok.ToString;

/**
 * This is a class to marshall the fields linking Employees and posts
 *
 * Methods imported by lombok:
 * toString
 */
@ToString
public class RequestEmployeePostAssignment {

  public String user_id;
  public String post_id;

}
