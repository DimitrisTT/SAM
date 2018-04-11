package com.tracktik.scheduler.api.domain;

import lombok.ToString;

@ToString
public class RequestHoliday {
  public String post_id;
  public String start_timestamp;
  public String end_timestamp;
}
