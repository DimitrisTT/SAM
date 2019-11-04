package com.tracktik.test;

import com.tracktik.scheduler.domain.*;
import org.drools.core.base.RuleNameEndsWithAgendaFilter;
import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.drools.core.base.RuleNameMatchesAgendaFilter;
import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
public class MinimumRestPeriodTest extends ConstraintRuleTestBase {

  @Test
  public void hasExactMinimumRestPeriod() {

    Employee employee = new Employee().setId("1").setMinimumRestPeriod(5L);

    Shift shift1 = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 01:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 02:00:00", dtf));

    Shift shift2 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 07:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 08:00:00", dtf));

    ksession.insert(employee);
    ksession.insert(shift1);
    ksession.insert(shift2);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter(".*Minimum Rest Period_PERIOD.*"));

    assertEquals(0L, getScoreHolder().getSoftScore());
  }

  @Test
  public void hasMoreThanMinimumRestPeriod() {

    Employee employee = new Employee().setId("1").setMinimumRestPeriod(5L);

    Shift shift1 = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 01:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 02:00:00", dtf));

    Shift shift2 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-18 07:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-18 08:00:00", dtf));

    ksession.insert(employee);
    ksession.insert(shift1);
    ksession.insert(shift2);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter(".*Minimum Rest Period_PERIOD.*"));

    assertEquals(0L, getScoreHolder().getSoftScore());
  }

  @Test
  public void hasLessThanMinimumRestPeriod() {

    Employee employee = new Employee().setId("1").setMinimumRestPeriod(5L);

    Shift shift1 = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 01:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 02:00:00", dtf));

    Shift shift2 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 06:30:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 08:00:00", dtf));

    ksession.insert(employee);
    ksession.insert(shift1);
    ksession.insert(shift2);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter(".*MINIMUM_REST_PERIOD.*"));

    assertEquals(-16L, getScoreHolder().getSoftScore());
  }

  @Test
  public void hasLessThanMinimumRestPeriodWithMultiplier() {

    Employee employee = new Employee().setId("1").setMinimumRestPeriod(5L);

    Shift shift1 = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 01:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 02:00:00", dtf));

    Shift shift2 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 06:30:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 08:00:00", dtf));

    EmployeeConstraintMultiplier employeeConstraintMultiplier = new EmployeeConstraintMultiplier()
        .setEmployeeId("1").setName("MINIMUM_REST_PERIOD").setMultiplier(5.0);

    ksession.insert(employeeConstraintMultiplier);
    ksession.insert(employee);
    ksession.insert(shift1);
    ksession.insert(shift2);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter(".*MINIMUM_REST_PERIOD.*"));

    assertEquals(-80L, getScoreHolder().getSoftScore());
  }

}

