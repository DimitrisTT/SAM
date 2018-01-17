package com.tracktik.scheduler.domain;

import java.util.ArrayList;
import java.util.List;

public class Employee {

	private String id;
	private AvailabilityPreference availabilityPreference;
	private List<Site> siteExperience = new ArrayList<Site>();
	private List<Skill> skills = new ArrayList<Skill>();
	private Long cost;	//times 100
	private Integer preferredHours = 40;
	private String name;
	
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
	public Integer getPreferredHours() {
		return preferredHours;
	}
	public void setPreferredHours(Integer preferredHours) {
		this.preferredHours = preferredHours;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((availabilityPreference == null) ? 0 : availabilityPreference.hashCode());
		result = prime * result + ((cost == null) ? 0 : cost.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((preferredHours == null) ? 0 : preferredHours.hashCode());
		result = prime * result + ((siteExperience == null) ? 0 : siteExperience.hashCode());
		result = prime * result + ((skills == null) ? 0 : skills.hashCode());
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
		if (availabilityPreference == null) {
			if (other.availabilityPreference != null)
				return false;
		} else if (!availabilityPreference.equals(other.availabilityPreference))
			return false;
		if (cost == null) {
			if (other.cost != null)
				return false;
		} else if (!cost.equals(other.cost))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (preferredHours == null) {
			if (other.preferredHours != null)
				return false;
		} else if (!preferredHours.equals(other.preferredHours))
			return false;
		if (siteExperience == null) {
			if (other.siteExperience != null)
				return false;
		} else if (!siteExperience.equals(other.siteExperience))
			return false;
		if (skills == null) {
			if (other.skills != null)
				return false;
		} else if (!skills.equals(other.skills))
			return false;
		return true;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
