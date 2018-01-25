package com.tracktik.scheduler.domain;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class EmployeeAvailability {

  private String employeeId;
  private AvailabilityType type;
  private DayOfWeek dayOfWeek;
  private LocalTime startTime;
  private LocalTime endTime;

  public String getEmployeeId() {
    return employeeId;
  }

  public EmployeeAvailability setEmployeeId(String employeeId) {
    this.employeeId = employeeId;
    return this;
  }

  public AvailabilityType getType() {
    return type;
  }

  public EmployeeAvailability setType(AvailabilityType type) {
    this.type = type;
    return this;
  }

  public DayOfWeek getDayOfWeek() {
    return dayOfWeek;
  }

  public EmployeeAvailability setDayOfWeek(DayOfWeek dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
    return this;
  }

  public LocalTime getStartTime() {
    return startTime;
  }

  public EmployeeAvailability setStartTime(LocalTime startTime) {
    this.startTime = startTime;
    return this;
  }

  public LocalTime getEndTime() {
    return endTime;
  }

  public EmployeeAvailability setEndTime(LocalTime endTime) {
    this.endTime = endTime;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    EmployeeAvailability that = (EmployeeAvailability) o;

    if (!employeeId.equals(that.employeeId)) return false;
    if (type != that.type) return false;
    if (dayOfWeek != that.dayOfWeek) return false;
    if (!startTime.equals(that.startTime)) return false;
    return endTime.equals(that.endTime);
  }

  @Override
  public int hashCode() {
    int result = employeeId.hashCode();
    result = 31 * result + type.hashCode();
    result = 31 * result + dayOfWeek.hashCode();
    result = 31 * result + startTime.hashCode();
    result = 31 * result + endTime.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "EmployeeAvailability{" +
        "employeeId='" + employeeId + '\'' +
        ", type=" + type +
        ", dayOfWeek=" + dayOfWeek +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        '}';
  }
}
