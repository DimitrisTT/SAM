package com.tracktik.scheduler.domain;

import java.util.HashSet;
import java.util.Set;

public class SchedulingResponseMetadata {

  private Boolean solution_is_feasible;
  private Long number_of_shifts_unfilled;
  private Long hard_constraint_score;
  private Long soft_constraint_score;
  private Long time_to_solve;
  private Set<ConstrainScore> constraint_scores = new HashSet<>();
  private Set<ShiftAssignmentConstraintScores> shift_assignment_scores = new HashSet<>();

  public SchedulingResponseMetadata() {
  }

  public Long getHard_constraint_score() {
    return hard_constraint_score;
  }

  public SchedulingResponseMetadata setHard_constraint_score(Long hard_constraint_score) {
    this.hard_constraint_score = hard_constraint_score;
    return this;
  }

  public Long getSoft_constraint_score() {
    return soft_constraint_score;
  }

  public SchedulingResponseMetadata setSoft_constraint_score(Long soft_constraint_score) {
    this.soft_constraint_score = soft_constraint_score;
    return this;
  }

  public Set<ConstrainScore> getConstraint_scores() {
    return constraint_scores;
  }

  public SchedulingResponseMetadata setConstraint_scores(Set<ConstrainScore> constraint_scores) {
    this.constraint_scores = constraint_scores;
    return this;
  }

  public Long getTime_to_solve() {
    return time_to_solve;
  }

  public SchedulingResponseMetadata setTime_to_solve(Long time_to_solve) {
    this.time_to_solve = time_to_solve;
    return this;
  }

  public Boolean getSolution_is_feasible() {
    return solution_is_feasible;
  }

  public SchedulingResponseMetadata setSolution_is_feasible(Boolean solution_is_feasible) {
    this.solution_is_feasible = solution_is_feasible;
    return this;
  }

  public Long getNumber_of_shifts_unfilled() {
    return number_of_shifts_unfilled;
  }

  public SchedulingResponseMetadata setNumber_of_shifts_unfilled(Long number_of_shifts_unfilled) {
    this.number_of_shifts_unfilled = number_of_shifts_unfilled;
    return this;
  }

  public Set<ShiftAssignmentConstraintScores> getShift_assignment_scores() {
    return shift_assignment_scores;
  }

  public SchedulingResponseMetadata setShift_assignment_scores(Set<ShiftAssignmentConstraintScores> shift_assignment_scores) {
    this.shift_assignment_scores = shift_assignment_scores;
    return this;
  }

  @Override
  public String toString() {
    return "SchedulingResponseMetadata{" +
        "solution_is_feasible=" + solution_is_feasible +
        ", number_of_shifts_unfilled=" + number_of_shifts_unfilled +
        ", hard_constraint_score=" + hard_constraint_score +
        ", soft_constraint_score=" + soft_constraint_score +
        ", time_to_solve=" + time_to_solve +
        ", constraint_scores=" + constraint_scores +
        ", shift_assignment_scores=" + shift_assignment_scores +
        '}';
  }
}
