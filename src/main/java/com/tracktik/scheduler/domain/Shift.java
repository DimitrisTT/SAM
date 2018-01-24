package com.tracktik.scheduler.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.time.Duration;

@PlanningEntity(difficultyComparatorClass = ShiftDifficultyComparator.class, movableEntitySelectionFilter = LockedShiftSelectionFilter.class)
public class Shift {

  private String id;
  private Boolean plan = false;
  private TimeSlot timeSlot;
  private Post post;

  @PlanningVariable(valueRangeProviderRefs = "employees")
  private Employee employee;

  public Shift() {
  }

  public TimeSlot getTimeSlot() {
    return timeSlot;
  }

  public Shift setTimeSlot(TimeSlot timeSlot) {
    this.timeSlot = timeSlot;
    return this;
  }

  public long getHours() {
    return Duration.between(timeSlot.getStart().toInstant(), timeSlot.getEnd().toInstant()).toHours();
  }

  public Post getPost() {
    return post;
  }


  public Shift setPost(Post post) {
    this.post = post;
    return this;
  }

  public Employee getEmployee() {
    return employee;
  }

  public Shift setEmployee(Employee employee) {
    this.employee = employee;
    return this;
  }

  public Boolean getPlan() {
    return plan;
  }

  public Shift setPlan(Boolean plan) {
    this.plan = plan;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Shift shift = (Shift) o;

    return id.equals(shift.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  public String getId() {
    return id;
  }

  public Shift setId(String id) {
    this.id = id;
    return this;
  }

  @Override
  public String toString() {
    return "Shift{" +
        "id='" + id + '\'' +
        ", plan=" + plan +
        ", timeSlot=" + timeSlot +
        ", post=" + post +
        ", employee=" + employee +
        '}';
  }
}
