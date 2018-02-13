package com.tracktik.scheduler.domain;

public class EmployeeConstraintMultiplier {

  private String employeeId;
  private String name;
  private Double multiplier = 1.0d;

  public String getEmployeeId() {
    return employeeId;
  }

  public EmployeeConstraintMultiplier setEmployeeId(String employeeId) {
    this.employeeId = employeeId;
    return this;
  }

  public String getName() {
    return name;
  }

  public EmployeeConstraintMultiplier setName(String name) {
    this.name = name;
    return this;
  }

  public Double getMultiplier() {
    return multiplier;
  }

  public EmployeeConstraintMultiplier setMultiplier(Double multiplier) {
    this.multiplier = multiplier;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    EmployeeConstraintMultiplier that = (EmployeeConstraintMultiplier) o;

    if (employeeId != null ? !employeeId.equals(that.employeeId) : that.employeeId != null) return false;
    if (name != null ? !name.equals(that.name) : that.name != null) return false;
    return multiplier.equals(that.multiplier);
  }

  @Override
  public int hashCode() {
    int result = employeeId != null ? employeeId.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + multiplier.hashCode();
    return result;
  }
}
