package com.tracktik.scheduler.domain;

public class ConstraintPreference {

  private String name;
  private Long weight;

  public String getName() {
    return name;
  }

  public ConstraintPreference setName(String name) {
    this.name = name;
    return this;
  }

  public Long getWeight() {
    return weight;
  }

  public ConstraintPreference setWeight(Long weight) {
    this.weight = weight;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ConstraintPreference that = (ConstraintPreference) o;

    if (!name.equals(that.name)) return false;
    return weight.equals(that.weight);
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + weight.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "ConstraintPreference{" +
        "name='" + name + '\'' +
        ", weight=" + weight +
        '}';
  }
}
