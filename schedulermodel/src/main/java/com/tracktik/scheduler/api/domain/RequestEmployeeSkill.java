package com.tracktik.scheduler.api.domain;

import lombok.ToString;

/**
 * This is a class to marshall the fields linking employees to their skills
 *
 * Methods imported by lombok:
 * toString
 */
@ToString
public class RequestEmployeeSkill {

  public String skill_id;
  public String employee_id;

}
