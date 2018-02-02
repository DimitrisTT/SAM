package com.tracktik.scheduler.domain;

public class SiteBan {

  private String siteId;
  private String employeeId;

  public String getSiteId() {
    return siteId;
  }

  public SiteBan setSiteId(String siteId) {
    this.siteId = siteId;
    return this;
  }

  public String getEmployeeId() {
    return employeeId;
  }

  public SiteBan setEmployeeId(String employeeId) {
    this.employeeId = employeeId;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SiteBan siteBan = (SiteBan) o;

    if (siteId != null ? !siteId.equals(siteBan.siteId) : siteBan.siteId != null) return false;
    return employeeId != null ? employeeId.equals(siteBan.employeeId) : siteBan.employeeId == null;
  }

  @Override
  public int hashCode() {
    int result = siteId != null ? siteId.hashCode() : 0;
    result = 31 * result + (employeeId != null ? employeeId.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "SiteBan{" +
        "siteId='" + siteId + '\'' +
        ", employeeId='" + employeeId + '\'' +
        '}';
  }
}
