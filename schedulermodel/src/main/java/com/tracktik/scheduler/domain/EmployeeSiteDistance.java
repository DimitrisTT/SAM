package com.tracktik.scheduler.domain;

public class EmployeeSiteDistance {

  private String employeeId;
  private String siteId;
  private Long distance;

  public EmployeeSiteDistance() {
  }

  public EmployeeSiteDistance(String employeeId, String siteId, Long distance) {
    this.employeeId = employeeId;
    this.siteId = siteId;
    this.distance = distance;
  }

  public String getEmployeeId() {
    return employeeId;
  }

  public EmployeeSiteDistance setEmployeeId(String employeeId) {
    this.employeeId = employeeId;
    return this;
  }

  public Long getDistance() {
    return distance;
  }

  public EmployeeSiteDistance setDistance(Long distance) {
    this.distance = distance;
    return this;
  }

  public String getSiteId() {
    return siteId;
  }

  public EmployeeSiteDistance setSiteId(String siteId) {
    this.siteId = siteId;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    EmployeeSiteDistance that = (EmployeeSiteDistance) o;

    if (employeeId != null ? !employeeId.equals(that.employeeId) : that.employeeId != null) return false;
    return siteId != null ? siteId.equals(that.siteId) : that.siteId == null;
  }

  @Override
  public int hashCode() {
    int result = employeeId != null ? employeeId.hashCode() : 0;
    result = 31 * result + (siteId != null ? siteId.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "EmployeeSiteDistance{" +
        "employeeId='" + employeeId + '\'' +
        ", siteId='" + siteId + '\'' +
        ", distance=" + distance +
        '}';
  }
}
