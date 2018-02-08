package com.tracktik.scheduler.domain;

import java.util.HashSet;
import java.util.Set;

public class ShiftAssignmentConstraintScores {
  private String shift_id;
  private String employee_id;
  private Set<ConstrainScore> scores = new HashSet<>();

  public String getShift_id() {
    return shift_id;
  }

  public ShiftAssignmentConstraintScores setShift_id(String shift_id) {
    this.shift_id = shift_id;
    return this;
  }

  public String getEmployee_id() {
    return employee_id;
  }

  public ShiftAssignmentConstraintScores setEmployee_id(String employee_id) {
    this.employee_id = employee_id;
    return this;
  }

  public Set<ConstrainScore> getScores() {
    return scores;
  }

  public ShiftAssignmentConstraintScores setScores(Set<ConstrainScore> scores) {
    this.scores = scores;
    return this;
  }

  @Override
  public String toString() {
    return "ShiftAssignmentConstraintScores{" +
        "shift_id='" + shift_id + '\'' +
        ", employee_id='" + employee_id + '\'' +
        ", scores=" + scores +
        '}';
  }
}
