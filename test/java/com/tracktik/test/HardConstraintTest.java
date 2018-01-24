package com.tracktik.test;

import com.tracktik.scheduler.domain.*;
import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScoreHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
public class HardConstraintTest extends ConstraintRuleTestBase {

  @Test
  public void testOverlappingShifts() {

    Employee employee = new Employee().setId("1");

    Shift shift1 = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setTimeSlot(new TimeSlot("2018-01-17 10:00:00", "2018-01-17 10:30:00"));

    Shift shift2 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setTimeSlot(new TimeSlot("2018-01-17 10:15:00", "2018-01-17 10:45:00"));

    ksession.insert(shift1);
    ksession.insert(shift2);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("employee can not work two shifts at same time"));

    assertTrue(getScoreHolder().getHardScore() < 0);
  }

  @Test
  public void testNonOverlappingShifts() {

    Employee employee = new Employee().setId("1");

    Shift shift1 = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setTimeSlot(new TimeSlot("2018-01-18 10:00:00", "2018-01-18 10:30:00"));

    Shift shift2 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setTimeSlot(new TimeSlot("2018-01-17 11:00:00", "2018-01-17 11:30:00"));

    ksession.insert(shift1);
    ksession.insert(shift2);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("employee can not work two shifts at same time"));

    assertEquals(0L, getScoreHolder().getHardScore());
  }

  @Test
  public void testEmployeeHasHardSkills() {

    Skill skill1 = new Skill().setId("skill1");
    Skill skill2 = new Skill().setId("skill2");
    Skill skill3 = new Skill().setId("skill3");

    List<Skill> employeeSkills = new ArrayList<>();
    employeeSkills.add(skill1);
    employeeSkills.add(skill2);
    employeeSkills.add(skill3);

    Employee employee = new Employee().setId("1");
    employee.setSkills(employeeSkills);

    Set<Skill> postSkills = new HashSet<>();
    postSkills.add(skill1);
    postSkills.add(skill2);
    postSkills.add(skill3);

    Post post = new Post().setHardSkills(postSkills);

    Shift shift = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setPost(post);

    ksession.insert(shift);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("employee must have hard skills"));

    assertEquals(0L, getScoreHolder().getHardScore());
  }

  @Test
  public void testEmployeeMissingHardSkills() {

    Skill skill1 = new Skill().setId("skill1");
    Skill skill2 = new Skill().setId("skill2");
    Skill skill3 = new Skill().setId("skill3");

    List<Skill> employeeSkills = new ArrayList<>();
    employeeSkills.add(skill1);
    employeeSkills.add(skill2);

    Employee employee = new Employee().setId("1");
    employee.setSkills(employeeSkills);

    Set<Skill> postSkills = new HashSet<>();
    postSkills.add(skill1);
    postSkills.add(skill2);
    postSkills.add(skill3);

    Post post = new Post().setHardSkills(postSkills);

    Shift shift = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setPost(post);

    ksession.insert(shift);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("employee must have hard skills"));


    assertTrue(getScoreHolder().getHardScore() < 0);
  }

}
