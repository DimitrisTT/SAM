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
  public Set<RequestOvertimeRule> overtime_rules = new HashSet<>();
  public Set<RequestPayrollSchedule> payroll_schedules = new HashSet<>();
  public Set<RequestHoliday> holidays = new HashSet<>();
  public Set<RequestFact> requestFacts = new HashSet<>();
  public Set<RequestScaleFact> scale_facts = new HashSet<>();
  public Set<RequestScale> scales = new HashSet<>();
  public Set<RequestEmployeeScale> employee_scales = new HashSet<>();

}
