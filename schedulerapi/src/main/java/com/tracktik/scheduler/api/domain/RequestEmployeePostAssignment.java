package com.tracktik.scheduler.api.domain;

public class RequestEmployeePostAssignment {

  public String user_id;
  public String post_id;

  @Override
  public String toString() {
    return "RequestEmployeeSiteAssignment{" +
        "user_id='" + user_id + '\'' +
        ", post_id='" + post_id + '\'' +
        '}';
  }
}
