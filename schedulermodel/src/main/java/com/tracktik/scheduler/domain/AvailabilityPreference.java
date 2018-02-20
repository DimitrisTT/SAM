package com.tracktik.scheduler.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;

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
