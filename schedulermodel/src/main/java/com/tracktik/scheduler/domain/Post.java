package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Accessors(chain = true)
@Data
@EqualsAndHashCode(of = "id")
public class Post {

  private String name;
  private String id;
  private Site site;
  private Long billRate; //times 100
  private Long payRate; //time 100
  private PayType payType;
  private Set<Skill> softSkills = new HashSet<Skill>();
  private Set<Skill> hardSkills = new HashSet<Skill>();

  public Long getNumberOfMatchingSoftSkills(Collection skills) {
    return softSkills.stream().filter(skills::contains).count();
  }

  public Long getNumberOfMatchingHardSkills(Collection skills) {
    return hardSkills.stream().filter(skills::contains).count();
  }

}
