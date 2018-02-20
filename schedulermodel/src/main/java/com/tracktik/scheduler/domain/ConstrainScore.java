package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(of = {"constraint_name"})
@Accessors(chain = true)
public class ConstrainScore {

  private String constraint_name;
  private Long soft_score;
  private Long hard_score;

  public ConstrainScore() {
  }

  public ConstrainScore(String constraint_name, Long soft_score, Long hard_score) {
    this.constraint_name = constraint_name;
    this.soft_score = soft_score;
    this.hard_score = hard_score;
  }

}
