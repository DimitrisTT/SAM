package com.tracktik.scheduler.domain;

public class CostTotals {

  private Employee employee;
  private long hours;
  private long totalBilled;
  private long totalCost;


  public CostTotals(Employee employee, long hours, long totalBilled, long totalCost) {
    this.employee = employee;
    this.hours = hours;
    this.totalBilled = totalBilled;
    this.totalCost = totalCost;
  }

  public long getHours() {
    return hours;
  }

  public void setHours(long hours) {
    this.hours = hours;
  }

  public long getTotalBilled() {
    return totalBilled;
  }

  public void setTotalBilled(long totalBilled) {
    this.totalBilled = totalBilled;
  }

  public long getTotalCost() {
    return totalCost;
  }

  public void setTotalCost(long totalCost) {
    this.totalCost = totalCost;
  }

  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  @Override
  public String toString() {
    return "CostTotals{" +
        "employee=" + employee +
        ", hours=" + hours +
        ", totalBilled=" + totalBilled +
        ", totalCost=" + totalCost +
        '}';
  }
}
