package com.tracktik.scheduler.domain;

import com.thoughtworks.xstream.annotations.XStreamConverter;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.persistence.xstream.api.score.buildin.hardsoftlong.HardSoftLongScoreXStreamConverter;

import java.util.HashSet;
import java.util.Set;

@PlanningSolution
public class Schedule {

  private String id;

  @XStreamConverter(HardSoftLongScoreXStreamConverter.class)
  private HardSoftLongScore score;

  @ValueRangeProvider(id = "employees")
  @ProblemFactCollectionProperty
  private Set<Employee> employees = new HashSet<>();

  @PlanningEntityCollectionProperty
  private Set<Shift> shifts = new HashSet<Shift>();

  @ProblemFactCollectionProperty
  private Set<Post> posts = new HashSet<Post>();

  @ProblemFactCollectionProperty
  private Set<Site> sites = new HashSet<Site>();

  @PlanningScore
  public HardSoftLongScore getScore() {
    return score;
  }

  public Schedule setScore(HardSoftLongScore score) {
    this.score = score;
    return this;
  }

  public Set<Employee> getEmployees() {
    return employees;
  }

  public Schedule setEmployees(Set<Employee> employees) {
    this.employees = employees;
    return this;
  }

  public Set<Shift> getShifts() {
    return shifts;
  }

  public Schedule setShifts(Set<Shift> shifts) {
    this.shifts = shifts;
    return this;
  }

  public Schedule addShift(Shift shift) {
    shifts.add(shift);
    return this;
  }

  public Schedule addEmployee(Employee employee) {
    employees.add(employee);
    return this;
  }

  public Set<Post> getPosts() {
    return posts;
  }

  public Schedule setPosts(Set<Post> posts) {
    this.posts = posts;
    return this;
  }

  public Set<Site> getSites() {
    return sites;
  }

  public Schedule setSites(Set<Site> sites) {
    this.sites = sites;
    return this;
  }

  public String getId() {
    return id;
  }

  public Schedule setId(String id) {
    this.id = id;
    return this;
  }

  @Override
  public String toString() {
    return "Schedule{" +
        "id='" + id + '\'' +
        ", score=" + score +
        ", employees=" + employees +
        ", shifts=" + shifts +
        ", posts=" + posts +
        ", sites=" + sites +
        '}';
  }
}
