package com.tracktik.scheduler.api.domain;

import lombok.ToString;

@ToString
public class RequestPostSkill {

  public String skill_id;
  public String post_id;
  public String type; //HARD, SOFT

}
