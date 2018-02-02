package com.tracktik.scheduler.domain;

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

  public String getConstraint_name() {
    return constraint_name;
  }

  public ConstrainScore setConstraint_name(String constraint_name) {
    this.constraint_name = constraint_name;
    return this;
  }

  public Long getSoft_score() {
    return soft_score;
  }

  public ConstrainScore setSoft_score(Long soft_score) {
    this.soft_score = soft_score;
    return this;
  }

  public Long getHard_score() {
    return hard_score;
  }

  public ConstrainScore setHard_score(Long hard_score) {
    this.hard_score = hard_score;
    return this;
  }

  @Override
  public String toString() {
    return "ConstrainScore{" +
        "constraint_name='" + constraint_name + '\'' +
        ", soft_score=" + soft_score +
        ", hard_score=" + hard_score +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ConstrainScore that = (ConstrainScore) o;

    return constraint_name.equals(that.constraint_name);
  }

  @Override
  public int hashCode() {
    return constraint_name.hashCode();
  }
}
