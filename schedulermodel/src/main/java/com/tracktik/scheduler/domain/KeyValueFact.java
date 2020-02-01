package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This is a KeyValueFact object, deprecated and replaced by Config Facts
 *
 * Methods imported by lombok:
 * Data
 * Accessors
 */
@Accessors(chain = true)
@Data
public class KeyValueFact {

  private String key;
  private Object value;

}
