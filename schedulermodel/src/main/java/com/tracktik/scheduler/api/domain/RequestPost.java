package com.tracktik.scheduler.api.domain;

public class RequestPost {

  public String id;
  public String name;
  public String pay_type; //EMPLOYEE_RATE, POST_RATE
  public String pay_rate; //find values
  public String site_id;
  public String bill_rate;  //

  @Override
  public String toString() {
    return "RequestPost{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", pay_type='" + pay_type + '\'' +
        ", pay_rate='" + pay_rate + '\'' +
        ", site_id='" + site_id + '\'' +
        ", bill_rate='" + bill_rate + '\'' +
        '}';
  }
}
