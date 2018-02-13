package com.tracktik.scheduler.api.domain;

public class RequestEmployee {

  public String id;
  public String name;
  public String geo_lat;
  public String geo_lon;
  public String pay_rate;
  public String seniority;
  public String preferred_hours;
  public String minimum_rest_period;
  public String previous_last_end_date_time;

  @Override
  public String toString() {
    return "RequestEmployee{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", geo_lat='" + geo_lat + '\'' +
        ", geo_lon='" + geo_lon + '\'' +
        ", pay_rate='" + pay_rate + '\'' +
        ", seniority='" + seniority + '\'' +
        ", preferred_hours='" + preferred_hours + '\'' +
        ", minimum_rest_period='" + minimum_rest_period + '\'' +
        ", previous_last_end_date_time='" + previous_last_end_date_time + '\'' +
        '}';
  }
}
