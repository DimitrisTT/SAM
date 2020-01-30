package com.tracktik.scheduler.api.domain;

import lombok.ToString;

/**
 * This is a class to marshall the fields related to posts and their info
 *
 * Methods imported by lombok:
 * toString
 */
@ToString
public class RequestPost {

  public String id;
  public String name;
  public String pay_type; //EMPLOYEE_RATE, POST_RATE
  public String pay_rate; //find values
  public String site_id;
  public String bill_rate;  //

}
