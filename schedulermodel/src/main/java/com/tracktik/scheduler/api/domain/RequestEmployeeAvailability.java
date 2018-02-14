package com.tracktik.scheduler.api.domain;

public class RequestEmployeeAvailability {

  public String employee_id;
  public String type;
  public String day_of_week;
  public String seconds_start;
  public String seconds_end;

  @Override
  public String toString() {
    return "RequestEmployeeAvailability{" +
        "employee_id='" + employee_id + '\'' +
        ", type='" + type + '\'' +
        ", day_of_week='" + day_of_week + '\'' +
        ", seconds_start='" + seconds_start + '\'' +
        ", seconds_end='" + seconds_end + '\'' +
        '}';
  }
}
