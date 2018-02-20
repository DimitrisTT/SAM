package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Accessors(chain = true)
@Data
public class SchedulingResponseMetadata {

  private Boolean solution_is_feasible;
  private Long number_of_shifts_unfilled;
  private Long hard_constraint_score;
  private Long soft_constraint_score;
  private Long time_to_solve;
  private Set<ConstrainScore> constraint_scores = new HashSet<>();
  private Set<ShiftAssignmentConstraintScores> shift_assignment_scores = new HashSet<>();

}
