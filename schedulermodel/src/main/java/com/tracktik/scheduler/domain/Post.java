package com.tracktik.scheduler.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Post {

  private String name;
  private String id;
  private Site site;
  private Long billRate; //times 100
  private Long payRate; //time 100
  private PayType payType;
  private Set<Skill> softSkills = new HashSet<Skill>();
  private Set<Skill> hardSkills = new HashSet<Skill>();

  public PayType getPayType() {
    return payType;
  }

  public Post setPayType(PayType payType) {
    this.payType = payType;
    return this;
  }

  public String getName() {
    return name;
  }

  public Post setName(String name) {
    this.name = name;
    return this;
  }

  public String getId() {
    return id;
  }

  public Post setId(String id) {
    this.id = id;
    return this;
  }

  public Site getSite() {
    return site;
  }

  public Post setSite(Site site) {
    this.site = site;
    return this;
  }

  public Long getBillRate() {
    return billRate;
  }

  public Post setBillRate(Long billRate) {
    this.billRate = billRate;
    return this;
  }

  public Long getPayRate() {
    return payRate;
  }

  public Post setPayRate(Long payRate) {
    this.payRate = payRate;
    return this;
  }

  public Set<Skill> getSoftSkills() {
    return softSkills;
  }

  public Post setSoftSkills(Set<Skill> softskills) {
    this.softSkills = softskills;
    return this;
  }

  public Set<Skill> getHardSkills() {
    return hardSkills;
  }

  public Post setHardSkills(Set<Skill> hardSkills) {
    this.hardSkills = hardSkills;
    return this;
  }

  public Long getNumberOfMatchingSoftSkills(Collection skills) {
    return softSkills.stream().filter(skills::contains).count();
  }

  public Long getNumberOfMatchingHardSkills(Collection skills) {
    return hardSkills.stream().filter(skills::contains).count();
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

  @Override
  public String toString() {
    return "Post{" +
        "name='" + name + '\'' +
        ", id='" + id + '\'' +
        ", site=" + site +
        ", billRate=" + billRate +
        ", payRate=" + payRate +
        ", payType=" + payType +
        ", softSkills=" + softSkills +
        ", hardSkills=" + hardSkills +
        '}';
  }
}
