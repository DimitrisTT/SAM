package com.tracktik.scheduler.domain;

import java.util.ArrayList;
import java.util.List;

public class Employee {

  private String id;
  private AvailabilityPreference availabilityPreference;
  private List<Site> siteExperience = new ArrayList<Site>();
  private List<Skill> skills = new ArrayList<Skill>();
  private Long cost;  //times 100
  private Long preferredHours = 40L;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<Site> getSiteExperience() {
    return siteExperience;
  }

  public void setSiteExperience(List<Site> siteExperience) {
    this.siteExperience = siteExperience;
  }

  public List<Skill> getSkills() {
    return skills;
  }

  public void setSkills(List<Skill> skills) {
    this.skills = skills;
  }

  public Long getCost() {
    return cost;
  }

  public void setCost(Long cost) {
    this.cost = cost;
  }

  public AvailabilityPreference getAvailabilityPreference() {
    return availabilityPreference;
  }

  public void setAvailabilityPreference(AvailabilityPreference availabilityPreference) {
    this.availabilityPreference = availabilityPreference;
  }

  public Long getPreferredHours() {
    return preferredHours;
  }

  public void setPreferredHours(Long preferredHours) {
    this.preferredHours = preferredHours;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Employee other = (Employee) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
}
