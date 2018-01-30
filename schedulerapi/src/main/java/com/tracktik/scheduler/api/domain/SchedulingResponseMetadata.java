package com.tracktik.scheduler.api.domain;

public class SchedulingResponseMetadata {

  public Long hard_constraint_score;
  public Long soft_constraint_score;

  public SchedulingResponseMetadata(Long hardScore, Long softScore) {
    hard_constraint_score = hardScore;
    soft_constraint_score = softScore;
  }
}
