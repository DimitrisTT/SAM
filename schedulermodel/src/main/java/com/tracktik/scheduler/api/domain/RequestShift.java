package com.tracktik.scheduler.api.domain;

import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

/**
 * This is a class to marshall the fields related to Shifts.
 *
 * Methods imported by lombok:
 * toString
 */
@ToString
public class RequestShift {

  public String shift_id;
  public String start_date_time;
  public String end_date_time;
  public String duration;
  public Long start_timestamp;
  public Long end_timestamp;
  public String post_id;
  public String start_date;
  public String plan;
  public String site_id;
  public String employee_id;
  public Set<String> tags = new HashSet<>();

}
