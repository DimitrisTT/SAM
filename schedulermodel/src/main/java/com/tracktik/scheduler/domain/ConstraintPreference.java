package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This is a class to hold the Constraint Preference
 *
 * Methods imported by lombok:
 * Data
 * Accessors
 */
@Accessors(chain = true)
@Data
public class ConstraintPreference {

  private String name;
  private Long weight;

}
