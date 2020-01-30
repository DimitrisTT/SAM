package com.tracktik.scheduler.api.domain;

import lombok.ToString;

/**
 * This is a class to marshall the fields related to Sites and their location
 *
 * Methods imported by lombok:
 * toString
 */
@ToString
public class RequestSite {

  public String id;
  public String geo_lat;
  public String geo_lon;
  public String name;

}
