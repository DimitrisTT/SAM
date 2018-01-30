package com.tracktik.scheduler.domain;

public class Skill {

  private String id;
  private String description;

  public Skill() {
  }

  public Skill(String sId, String sDescription) {
    id = sId;
    description = sDescription;
  }

  public String getId() {
    return id;
  }

  public Skill setId(String id) {
    this.id = id;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public Skill setDescription(String description) {
    this.description = description;
    return this;
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
    Skill other = (Skill) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Skill{" +
        "id='" + id + '\'' +
        ", description='" + description + '\'' +
        '}';
  }
}
