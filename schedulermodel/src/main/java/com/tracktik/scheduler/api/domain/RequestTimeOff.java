package com.tracktik.scheduler.api.domain;

import lombok.ToString;

@ToString
public class RequestTimeOff {

  public String start_time;
  public String end_time;
  public String employee_id;

}
