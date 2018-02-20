package com.tracktik.scheduler.api.domain;

import lombok.ToString;

@ToString
public class RequestEmployeeAvailability {

  public String employee_id;
  public String type;
  public String day_of_week;
  public String seconds_start;
  public String seconds_end;

}
