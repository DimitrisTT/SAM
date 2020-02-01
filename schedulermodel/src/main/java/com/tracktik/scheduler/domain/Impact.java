package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This is the impact object used by scalefacts
 * used to calculate value of difference between scalefact and scale
 *
 * Methods imported by lombok:
 * Data
 * Accessors
 */
@Accessors(chain = true)
@Data
public class Impact {

  private Boolean square = false;
  private int impact;

  public Impact(){

  }

  /*
   * Constructor with fields
   * @param square is to signify if the value should be calculated exponentially or linearly
   * @param impact is a multiplicative coefficient to use
   */
  public Impact(Boolean square, Integer impact) {
    this.square = square;
    this.impact = impact;
  }
}
