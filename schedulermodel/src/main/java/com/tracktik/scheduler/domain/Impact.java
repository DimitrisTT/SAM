package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class Impact {

  private Boolean square = false;
  private Integer impact;

  public Impact(Boolean square, Integer impact) {
    this.square = square;
    this.impact = impact;
  }
}
