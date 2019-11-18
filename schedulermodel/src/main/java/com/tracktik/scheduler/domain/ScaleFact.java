package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class ScaleFact {

  private ScaleTag scaleTag;
  private ScaleType scaleType;
  private Integer rating;
  private Integer postId;
  private Impact impact;

}
