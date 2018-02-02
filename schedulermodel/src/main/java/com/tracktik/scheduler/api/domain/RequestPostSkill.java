package com.tracktik.scheduler.api.domain;

public class RequestPostSkill {

  public String skill_id;
  public String post_id;
  public String type; //HARD, SOFT

  @Override
  public String toString() {
    return "RequestPostSkill{" +
        "skill_id='" + skill_id + '\'' +
        ", post_id='" + post_id + '\'' +
        ", type='" + type + '\'' +
        '}';
  }
}
