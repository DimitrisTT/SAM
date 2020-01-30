package com.tracktik.scheduler.api.domain;

import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * This is a class to marshall the fields related to overtime rules and their criteria
 *
 * Methods imported by lombok:
 * toString
 */
@ToString
public class RequestOvertimeRule {
  public String id;
  public String name;
  public Map<String, List<List>> rule;
}
