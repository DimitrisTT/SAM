package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Accessors(chain = true)
@Data
@EqualsAndHashCode(exclude = "scores")
public class ShiftAssignmentConstraintScores {
  private String shift_id;
  private String employee_id;
  private Set<ConstraintScore> scores = new HashSet<>();

}
