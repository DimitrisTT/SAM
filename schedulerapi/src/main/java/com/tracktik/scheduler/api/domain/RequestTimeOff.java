package com.tracktik.scheduler.api.domain;

public class RequestTimeOff {

  public String start_time;
  public String end_time;
  public String employee_id;

  @Override
  public String toString() {
    return "RequestTimeOff{" +
        "start_time_long='" + start_time + '\'' +
        ", end_time_long='" + end_time + '\'' +
        ", employee_id='" + employee_id + '\'' +
        '}';
  }
}
