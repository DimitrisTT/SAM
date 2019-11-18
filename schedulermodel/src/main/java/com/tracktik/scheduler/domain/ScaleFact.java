package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class ScaleFact {

  private Integer id;
  private ScaleTag scaleTag;
  private ScaleType scaleType;
  private Integer rating;
  private Integer postId;
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
}
