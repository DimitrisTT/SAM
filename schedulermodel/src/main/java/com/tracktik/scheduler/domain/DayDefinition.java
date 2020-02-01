package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This is a class to hold the Day Definition, as the work week may start on any hour
 *
 * Methods imported by lombok:
 * Data
 * Accessors
 */
@Accessors(chain = true)
@Data
public class DayDefinition {

  private Integer startHour;
}
