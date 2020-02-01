package com.tracktik.scheduler.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;


/**
 * This is a class to hold the Availability Preference as chosen by the Employees
 *
 * Methods imported by lombok:
 * toString
 * Equals and HashCode
 */
@ToString
@EqualsAndHashCode()
public class AvailabilityPreference {

  private Integer requestedHoursPerWeek;


  public Integer getRequestedHoursPerWeek() {
    return requestedHoursPerWeek;
  }

  public void setRequestedHoursPerWeek(Integer requestedHoursPerWeek) {
    this.requestedHoursPerWeek = requestedHoursPerWeek;
  }

}
