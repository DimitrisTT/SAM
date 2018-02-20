package com.tracktik.scheduler.api.domain;

import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ToString
public class RequestForScheduling {

  Logger logger = LoggerFactory.getLogger(RequestForScheduling.class);

  public Set<RequestShift> shifts = new HashSet<>();
  public Set<RequestPost> posts = new HashSet<>();
  public Set<RequestSkill> skills = new HashSet<>();
  public Set<RequestPostSkill> post_skills = new HashSet<>();
  public Set<RequestSite> sites = new HashSet<>();
  public Set<RequestEmployee> employees = new HashSet<>();
  public Set<RequestEmployeeSkill> employee_skills = new HashSet<>();
  public Set<RequestEmployeePostAssignment> employees_to_posts = new HashSet<>();
  public Set<RequestEmployeeSiteAssignment> employees_to_sites = new HashSet<>();
  public Set<RequestTimeOff> time_off = new HashSet<>();
  public Set<RequestEmployeeAvailability> employee_availabilities = new HashSet<>();
  public Map<String, Object> facts = new HashMap<>();
  public Set<RequestSiteBan> site_bans = new HashSet<>();
  public Map<String, Map<String, String>> employee_multipliers = new HashMap<>();

}
