package com.tracktik.scheduler.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class TimeOff {

  private String employeeId;
  private Date startTime;
  private Date endTime;

  public TimeOff() {
  }

  public TimeOff(String employeeId, Date startTime, Date endTime) {
    this.employeeId = employeeId;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public String getEmployeeId() {
    return employeeId;
  }

  public TimeOff setEmployeeId(String employeeId) {
    this.employeeId = employeeId;
    return this;
  }

  public Date getStartTime() {
    return startTime;
  }

  public TimeOff setStartTime(Date startTime) {
    this.startTime = startTime;
    return this;
  }

  public Date getEndTime() {
    return endTime;
  }

  public TimeOff setEndTime(Date endTime) {
    this.endTime = endTime;
    return this;
  }

  @JsonIgnore
  public LocalTime getStartLocalTime() {
    return LocalDateTime.ofInstant(startTime.toInstant(), ZoneId.systemDefault()).toLocalTime();
  }

  @JsonIgnore
  public LocalTime getEndLocalTime() {
    return LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault()).toLocalTime();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TimeOff timeOff = (TimeOff) o;

    if (!employeeId.equals(timeOff.employeeId)) return false;
    if (!startTime.equals(timeOff.startTime)) return false;
    return endTime.equals(timeOff.endTime);
  }

  @Override
  public int hashCode() {
    int result = employeeId.hashCode();
    result = 31 * result + startTime.hashCode();
    result = 31 * result + endTime.hashCode();
    return result;
  }
}
