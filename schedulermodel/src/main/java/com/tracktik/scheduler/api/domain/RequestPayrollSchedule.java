package com.tracktik.scheduler.api.domain;

import lombok.ToString;

@ToString
public class RequestPayrollSchedule {
  public String id;
  public String name;
  public String frequency;
  public String period_start_time;
  public String period_start_date;
  public String overlapping_method;
  public String count_holiday_hours_towards_period_ot;
  public String align_holidays_with_period_start_time;
}
