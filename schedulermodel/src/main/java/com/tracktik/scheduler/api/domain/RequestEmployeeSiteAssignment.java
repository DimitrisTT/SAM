package com.tracktik.scheduler.api.domain;

public class RequestEmployeeSiteAssignment {

  public String user_id;
  public String site_id;

  @Override
  public String toString() {
    return "RequestEmployeeSiteAssignment{" +
        "user_id='" + user_id + '\'' +
        ", site_id='" + site_id + '\'' +
        '}';
  }
}
