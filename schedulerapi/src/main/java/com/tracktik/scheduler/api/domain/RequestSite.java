package com.tracktik.scheduler.api.domain;

public class RequestSite {

  public String id;
  public String geo_lat;
  public String geo_lon;
  public String name;

  @Override
  public String toString() {
    return "RequestSite{" +
        "id='" + id + '\'' +
        ", geo_lat='" + geo_lat + '\'' +
        ", geo_lon='" + geo_lon + '\'' +
        ", name='" + name + '\'' +
        '}';
  }
}
