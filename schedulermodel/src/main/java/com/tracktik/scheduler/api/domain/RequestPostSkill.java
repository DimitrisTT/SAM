package com.tracktik.scheduler.api.domain;

import lombok.ToString;

/**
 * This is a class to marshall the fields related to skills that posts require
 * For configurative purposes certain skills may need to be connected to FactTypes at this stage
 * see RequestFactType.java
 *
 * Methods imported by lombok:
 * toString
 */
@ToString
public class RequestPostSkill {

  public String skill_id;
  public String post_id;
  public String type; //HARD, SOFT

  public enum FactType {
      FAR_FROM_SITE,
      FAIRLY_FAR_FROM_SITE,
      NEAR_BY_SITE,
      HARD_SKILL_MISSING,
      SOFT_SKILL_MISSING,
      NOT_AVAILABLE,
      MAYBE_AVAILABLE,
      MINIMUM_REST_PERIOD,
      LESS_THAN_EXPECTED_HOURS,
      MORE_THEN_EXPECTED_HOURS,
      NO_EXPERIENCE_AT_SITE,
      NOT_ASSIGNED_TO_SITE
  }
}
