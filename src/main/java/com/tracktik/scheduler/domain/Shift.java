package com.tracktik.scheduler.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity()
public class Shift {

  private TimeSlot timeSlot;
  private Post post;

  @PlanningVariable(valueRangeProviderRefs = "employees")
  private Employee employee;

  public TimeSlot getTimeSlot() {
    return timeSlot;
  }

  public Shift setTimeSlot(TimeSlot timeSlot) {
    this.timeSlot = timeSlot;
    return this;
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((employee == null) ? 0 : employee.hashCode());
    result = prime * result + ((post == null) ? 0 : post.hashCode());
    result = prime * result + ((timeSlot == null) ? 0 : timeSlot.hashCode());
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
    Shift other = (Shift) obj;
    if (employee == null) {
      if (other.employee != null)
        return false;
    } else if (!employee.equals(other.employee))
      return false;
    if (post == null) {
      if (other.post != null)
        return false;
    } else if (!post.equals(other.post))
      return false;
    if (timeSlot == null) {
      if (other.timeSlot != null)
        return false;
    } else if (!timeSlot.equals(other.timeSlot))
      return false;
    return true;
  }

}
