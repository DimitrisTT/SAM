package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class ScaleFact {

  private int id;
  private ScaleTag scaleTag;
  private ScaleType scaleType;
  private int rating;
  private int postId;
  private Impact impact;

  public ScaleFact() {

  }

  public ScaleFact(Integer id, ScaleTag scaleTag, ScaleType scaleType, Integer rating, Integer postId, Impact impact) {
    this.id = id;
    this.scaleTag = scaleTag;
    this.scaleType = scaleType;
    this.rating = rating;
    this.postId = postId;
    this.impact = impact;
  }

  public int getRating() {
    return rating;
  }

  public Impact getImpact() {
    return impact;
  }
}
