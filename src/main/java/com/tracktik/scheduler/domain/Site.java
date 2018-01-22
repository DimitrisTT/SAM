package com.tracktik.scheduler.domain;

public class Site {

  private String id;
  private String name;
  private Double latitude;
  private Double longitude;

  public Double getLongitude() {
    return longitude;
  }

  public Site setLongitude(Double longitude) {
    this.longitude = longitude;
    return this;
  }

  public Double getLatitude() {
    return latitude;
  }

  public Site setLatitude(Double latitude) {
    this.latitude = latitude;
    return this;
  }

  public String getName() {
    return name;
  }

  public Site setName(String name) {
    this.name = name;
    return this;
  }

  public String getId() {
    return id;
  }

  public Site setId(String id) {
    this.id = id;
    return this;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Site other = (Site) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

}
