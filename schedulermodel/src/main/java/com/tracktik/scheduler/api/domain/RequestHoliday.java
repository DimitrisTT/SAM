package com.tracktik.scheduler.api.domain;

import lombok.ToString;

/**
 * This is a class to marshall the fields related to Holidays on posts
 *
 * Methods imported by lombok:
 * toString
 */
@ToString
public class RequestHoliday {
  public String post_id;
  public String start_timestamp;
  public String end_timestamp;
}
