package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.*;

/**
 * This is a class to hold the Employee and all of its various fields
 * All of the Request objects that were linked to employees in the Json are added into this object by this point.
 *
 * Methods imported by lombok:
 * Data
 * Accessors
 * Equals and Hash Code
 */
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

  /*
   * This method sets the cost field to be 100 times the pay_rate
   * @param pay_rate is the string to be converted to a number and used for calculation
   */
  public Employee setCostFromFloatString(String pay_rate) {
    if (pay_rate != null && !pay_rate.isEmpty()) {
      Float rate = new Float(pay_rate);
      rate = rate * 100;
      this.setCost(rate.longValue());
    }

    return this;
  }

  /*
   * This method checks for matching shift tags tagged in the employee
   * @param shiftTags is the Set of shift tags to check against
   */
  public Boolean hasShiftTags(Set<String> shiftTags) {

    for (String key : tagValues.keySet()) {
      if (shiftTags.contains(key)) {
        return true;
      }
    }
    return false;
  }

  /*
   * This method returns a sum of all the tag values currently on the set of shiftTags given
   * @param shiftTags the set to check against
   */
  public Long tagValueSummary(Set<String> shiftTags) {

    return tagValues.entrySet().stream()
        .filter(stringLongEntry -> shiftTags.contains(stringLongEntry.getKey()))
        .map(Map.Entry::getValue).mapToLong(Long::longValue).sum();
  }

  /*
   * This method adds site experience to an employees running list
   * @param siteExperienced the site experience to add
   */
  public void addSiteExperience(Site siteExperienced) {
    this.siteExperience.add(siteExperienced);
  }

  /*
   * This method adds post experience to an employees running list
   * @param postExperienced the post experience to add
   */
  public void addPostExperience(Post postExperienced) {
    this.postExperience.add(postExperienced);
  }

}
