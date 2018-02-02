package com.tracktik.scheduler.api.domain;

public class RequestSkill {

  public String id;
  public String description;

  @Override
  public String toString() {
    return "RequestSkill{" +
        "id='" + id + '\'' +
        ", description='" + description + '\'' +
        '}';
  }
}
