package com.tracktik.scheduler.api.domain;

import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
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
  public Map<String, Object> tag_values = new HashMap<>();

}
