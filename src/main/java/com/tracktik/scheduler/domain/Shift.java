package com.tracktik.scheduler.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.time.Duration;

@PlanningEntity(difficultyComparatorClass = ShiftDifficultyComparator.class)
public class Shift {

  private String id;
  private Boolean locked;
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

  public Boolean getLocked() {
    return locked;
  }

  public Shift setLocked(Boolean locked) {
    this.locked = locked;
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
        "timeSlot=" + timeSlot +
        ", post id=" + post.getId() +
        ", id='" + id + '\'' +
        ", employee id=" + ((employee == null) ? " none" : employee.getId()) +
        '}';
  }
}
