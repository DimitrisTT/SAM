package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * This is a class to hold the Constraint Score, we use both hard and soft in this project
 *
 * Methods imported by lombok:
 * Data
 * Accessors
 * Equals and Hash Code
 */
@Data
@EqualsAndHashCode(of = {"constraint_name"})
@Accessors(chain = true)
public class ConstraintScore {

  private String constraint_name;
  private Long soft_score;
  private Long hard_score;

  public ConstraintScore() {
  }

      /* This is the basic constructor
     * @param constraint_name is the name of the constraint score to be used
     * @param soft_score the starting soft score in the calculator
     * @param hard_score the starting hard score in the calculator
     */
  public ConstraintScore(String constraint_name, Long soft_score, Long hard_score) {
    this.constraint_name = constraint_name;
    this.soft_score = soft_score;
    this.hard_score = hard_score;
  }

}
