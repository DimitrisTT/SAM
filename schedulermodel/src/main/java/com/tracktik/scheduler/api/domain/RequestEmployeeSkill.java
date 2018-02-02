package com.tracktik.scheduler.api.domain;

public class RequestEmployeeSkill {

  public String skill_id;
  public String employee_id;

  @Override
  public String toString() {
    return "RequestEmployeeSkill{" +
        "skill_id='" + skill_id + '\'' +
        ", employee_id='" + employee_id + '\'' +
        '}';
  }
}
