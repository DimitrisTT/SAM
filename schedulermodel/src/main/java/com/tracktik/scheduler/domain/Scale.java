package com.tracktik.scheduler.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Scale {

  private String id;
  private ScaleTag scaleTag;
  private Integer rating;

  public String getId() {
    return id;
  }

  public ScaleTag getScaleTag() {
    return scaleTag;
  }

  public Integer getRating() {
    return rating;
  }
}
