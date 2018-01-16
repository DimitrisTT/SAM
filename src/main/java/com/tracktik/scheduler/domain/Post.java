package com.tracktik.scheduler.domain;

import java.util.ArrayList;
import java.util.List;

public class Post {

	private String id;
	private Site site;
	private Long billRate; //times 100
	private Long payRate; //time 100
	private Boolean useEmployeePayRate;
	private List<Skill> softskills = new ArrayList<Skill>();
	private List<Skill> hardSkills = new ArrayList<Skill>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Site getSite() {
		return site;
	}
	public void setSite(Site site) {
		this.site = site;
	}
	public Long getBillRate() {
		return billRate;
	}
	public void setBillRate(Long billRate) {
		this.billRate = billRate;
	}
	public Long getPayRate() {
		return payRate;
	}
	public void setPayRate(Long payRate) {
		this.payRate = payRate;
	}
	public Boolean getUseEmployeePayRate() {
		return useEmployeePayRate;
	}
	public void setUseEmployeePayRate(Boolean useEmployeePayRate) {
		this.useEmployeePayRate = useEmployeePayRate;
	}
	public List<Skill> getSoftskills() {
		return softskills;
	}
	public void setSoftskills(List<Skill> softskills) {
		this.softskills = softskills;
	}
	public List<Skill> getHardSkills() {
		return hardSkills;
	}
	public void setHardSkills(List<Skill> hardSkills) {
		this.hardSkills = hardSkills;
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
		Post other = (Post) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
