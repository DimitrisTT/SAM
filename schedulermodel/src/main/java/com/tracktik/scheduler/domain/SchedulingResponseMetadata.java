package com.tracktik.scheduler.domain;

import java.util.HashSet;
import java.util.Set;

public class SchedulingResponseMetadata {

  private Long hard_constraint_score;
  private Long soft_constraint_score;
  private Set<ConstrainScore> constraint_scores = new HashSet<>();

  public SchedulingResponseMetadata() {
  }
/*
  public SchedulingResponseMetadata(Long hardScore, Long softScore, Set constraintScores) {
    hard_constraint_score = hardScore;
    soft_constraint_score = softScore;
    constraint_scores = constraintScores;
  }*/

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

  @Override
  public String toString() {
    return "SchedulingResponseMetadata{" +
        "hard_constraint_score=" + hard_constraint_score +
        ", soft_constraint_score=" + soft_constraint_score +
        ", constraint_scores=" + constraint_scores +
        '}';
  }

}
