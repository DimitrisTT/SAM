package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@EqualsAndHashCode(of = "id")
public class Site {

  private String id;
  private String name;
  private Double latitude;
  private Double longitude;

}
