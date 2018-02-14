package com.tracktik.scheduler.api.domain;

public class RequestSiteBan {
  public String site_id;
  public String employee_id;

  @Override
  public String toString() {
    return "RequestSiteBan{" +
        "site_id='" + site_id + '\'' +
        ", employee_id='" + employee_id + '\'' +
        '}';
  }
}
