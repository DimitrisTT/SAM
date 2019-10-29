package com.tracktik.scheduler.domain;

import com.thoughtworks.xstream.annotations.XStreamConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
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
@Accessors(chain = true)
@Data
@EqualsAndHashCode(of = "id")
public class Schedule {

  private String id;

  @XStreamConverter(HardSoftLongScoreXStreamConverter.class)
  @PlanningScore
  private HardSoftLongScore score;

  @ValueRangeProvider(id = "employees")
  @ProblemFactCollectionProperty
  private Set<Employee> employees = new HashSet<>();

  @PlanningEntityCollectionProperty
  private Set<Shift> shifts = new HashSet<>();

  @ProblemFactCollectionProperty
  private Set<Post> posts = new HashSet<>();

  @ProblemFactCollectionProperty
  private Set<Site> sites = new HashSet<>();

  @ProblemFactCollectionProperty
  private Set<TimeOff> timesOff = new HashSet<>();

  @ProblemFactCollectionProperty
  private Set<EmployeeAvailability> employeeAvailabilities = new HashSet<>();

  @ProblemFactCollectionProperty
  private Set<ConstraintPreference> constraintPreferences = new HashSet<>();

  @ProblemFactCollectionProperty
  private Set<EmployeeSiteDistance> employeeSiteDistance = new HashSet<>();

  @ProblemFactCollectionProperty
  private Set<KeyValueFact> keyValueFacts = new HashSet<>();

  @ProblemFactCollectionProperty
  private Set<SiteBan> siteBans = new HashSet<>();

  @ProblemFactCollectionProperty
  private Set<EmployeeConstraintMultiplier> employeeConstraintMultipliers = new HashSet<>();

  @ProblemFactCollectionProperty
  private Set<PeriodOvertimeDefinition> periodOvertimeDefinitions = new HashSet<>();

  @ProblemFactCollectionProperty
  private Set<DayOvertimeDefinition> dayOvertimeDefinitions = new HashSet<>();

  @ProblemFactCollectionProperty
  private Set<ConsecutiveDaysOvertimeDefinition> consecutiveDaysOvertimeDefinitions = new HashSet<>();

  @ProblemFactCollectionProperty
  private Set<PayrollSchedule> payrollSchedules = new HashSet<>();

  @ProblemFactCollectionProperty
  private Set<HolidayPeriod> holidayPeriods = new HashSet<>();

  @ProblemFactCollectionProperty
  private Set<ConfigFact> configFacts = new HashSet<>();

  public Schedule addEmployee(Employee employee) {
    employees.add(employee);
    return this;
  }
}
