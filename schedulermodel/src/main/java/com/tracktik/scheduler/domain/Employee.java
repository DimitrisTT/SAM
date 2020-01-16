package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.*;

@Accessors(chain = true)
@EqualsAndHashCode(of = "id")
@Data
public class Employee {

  private String id;
  private String name;
  private AvailabilityPreference availabilityPreference;
  private List<Site> siteExperience = new ArrayList<>();
  private List<Post> postExperience = new ArrayList<>();
  private List<Skill> skills = new ArrayList<>();
  private List<Scale> scales = new ArrayList<>();
  private Long cost;  //times 100
  private Long preferredHours;
  private Double latitude;
  private Double longitude;
  private Integer seniority;
  private Long minimumRestPeriod;
  private Map<String, Long> tagValues = new HashMap<>();
  private String overtimeRuleId;
  private String payScheduleId;
  private LocalDateTime previousPayPeriodEnd;
  private Clockwise clockwise;

  public Employee setCostFromFloatString(String pay_rate) {
    if (pay_rate != null && !pay_rate.isEmpty()) {
      Float rate = new Float(pay_rate);
      rate = rate * 100;
      this.setCost(rate.longValue());
    }

    return this;
  }

  public Boolean hasShiftTags(Set<String> shiftTags) {

    for (String key : tagValues.keySet()) {
      if (shiftTags.contains(key)) {
        return true;
      }
    }
    return false;
  }

  public Long tagValueSummary(Set<String> shiftTags) {

    return tagValues.entrySet().stream()
        .filter(stringLongEntry -> shiftTags.contains(stringLongEntry.getKey()))
        .map(Map.Entry::getValue).mapToLong(Long::longValue).sum();
  }

  public void addSiteExperience(Site siteExperienced) {
    this.siteExperience.add(siteExperienced);
  }

  public void addPostExperience(Post postExperienced) {
    this.postExperience.add(postExperienced);
  }



}
