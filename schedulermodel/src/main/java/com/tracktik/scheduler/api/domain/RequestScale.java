package com.tracktik.scheduler.api.domain;

import lombok.ToString;

/**
 * This is a class to marshall the fields related to Scales, the values assigned to employees.
 * When calculated by optaplanner, the fields get compared to the scalefacts set to certain posts.
 *
 * Methods imported by lombok:
 * toString
 */
@ToString
public class RequestScale {

  public String id;
  public String tag;
  public String rating;

}
