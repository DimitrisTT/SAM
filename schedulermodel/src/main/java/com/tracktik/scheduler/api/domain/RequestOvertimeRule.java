package com.tracktik.scheduler.api.domain;

import lombok.ToString;

import java.util.List;
import java.util.Map;

@ToString
public class RequestOvertimeRule {
  public String id;
  public String name;
  public Map<String, List<List>> rule;
}
