package com.tracktik.test;

import com.tracktik.scheduler.domain.*;
import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Ignore;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
public class HardConstraintTest extends ConstraintRuleTestBase {

  @Ignore
  public void testOverlappingShifts() {

    Employee employee = new Employee().setId("1");

    Shift shift1 = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 10:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 10:30:00", dtf));

    Shift shift2 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 10:15:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 10:45:00", dtf));

    ksession.insert(shift1);
    ksession.insert(shift2);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("CAN_NOT_WORK_SIMULTANEOUS_SHIFTS"));

    assertTrue(getScoreHolder().getHardScore() < 0);
  }

  @Test
  public void testNonOverlappingShifts() {

    Employee employee = new Employee().setId("1");

    Shift shift1 = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-18 10:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-18 10:30:00", dtf));

    Shift shift2 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 11:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 11:30:00", dtf));

    ksession.insert(shift1);
    ksession.insert(shift2);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("CAN_NOT_WORK_SIMULTANEOUS_SHIFTS"));

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
        .setPost(post)
        .setStart(LocalDateTime.parse("2018-01-17 04:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 05:00:00", dtf));

    ksession.insert(shift);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("MUST_HAVE_HARD_SKILLS"));

    assertEquals(0L, getScoreHolder().getHardScore());
  }

  @Test
  public void testHardSkillsEnabled() {

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
        .setPost(post)
        .setStart(LocalDateTime.parse("2018-01-17 04:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 05:00:00", dtf));

    KeyValueFact hardSkillsEnabled = new KeyValueFact().setKey("HARD_SKILL_ENABLED").setValue(true);

    ksession.insert(shift);
    ksession.insert(hardSkillsEnabled);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("MUST_HAVE_HARD_SKILLS"));

    assertEquals(0L, getScoreHolder().getHardScore());
  }

  @Ignore
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
        .setPost(post)
        .setStart(LocalDateTime.parse("2018-01-17 04:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 05:00:00", dtf));

    ksession.insert(shift);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("MUST_HAVE_HARD_SKILLS"));


    assertTrue(getScoreHolder().getHardScore() < 0);
  }

  @Test
  public void testHardSkillsDisabled() {

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
        .setPost(post)
        .setStart(LocalDateTime.parse("2018-01-17 04:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 05:00:00", dtf));

    KeyValueFact hardSkillsDisabled = new KeyValueFact().setKey("HARD_SKILL_ENABLED").setValue(false);

    ksession.insert(shift);
    ksession.insert(hardSkillsDisabled);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("MUST_HAVE_HARD_SKILLS"));

    assertEquals(0L, getScoreHolder().getHardScore());
  }

  @Test
  public void testEmployeeIsBannedFromSite() {

    Site site = new Site().setId("1");

    Employee employee = new Employee().setId("2");

    Post post = new Post()
        .setSite(site).setId("3");
    Shift shift = new Shift().setId("4")
        .setEmployee(employee).setPost(post)
        .setStart(LocalDateTime.parse("2018-01-17 04:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 05:00:00", dtf));

    SiteBan ban = new SiteBan().setEmployeeId("2").setSiteId("1");

    ksession.insert(site);
    ksession.insert(employee);
    ksession.insert(post);
    ksession.insert(shift);
    ksession.insert(ban);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("BANNED_FROM_SITE"));

    assertEquals(-1L, getScoreHolder().getHardScore());

  }

  @Test
  public void employeeHasTimeoffDuringShift() throws ParseException {

    Employee employee = new Employee().setId("1");

    Shift shift = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 11:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 11:30:00", dtf));

    TimeOff timeOff = new TimeOff().setEmployeeId("1")
        .setStart(LocalDateTime.parse("2018-01-01 00:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-20 00:00:00", dtf));

    ksession.insert(employee);
    ksession.insert(shift);
    ksession.insert(timeOff);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("TIME_OFF_DURING_SHIFT"));

    assertTrue(getScoreHolder().getHardScore() < 0);
  }

  @Test
  public void employeeTimeoffOverlapsStartOfShift() throws ParseException {

    Employee employee = new Employee().setId("1");

    Shift shift = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 11:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 11:30:00", dtf));

    TimeOff timeOff = new TimeOff().setEmployeeId("1")
        .setStart(LocalDateTime.parse("2018-01-01 00:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 11:00:01", dtf));

    ksession.insert(employee);
    ksession.insert(shift);
    ksession.insert(timeOff);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("TIME_OFF_DURING_SHIFT"));

    assertTrue(getScoreHolder().getHardScore() < 0);
  }

  @Test
  public void employeeTimeoffOverlapsEndOfShift() throws ParseException {

    Employee employee = new Employee().setId("1");

    Shift shift = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 11:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 11:30:00", dtf));

    TimeOff timeOff = new TimeOff().setEmployeeId("1")
        .setStart(LocalDateTime.parse("2018-01-17 11:29:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-20 00:00:00", dtf));

    ksession.insert(employee);
    ksession.insert(shift);
    ksession.insert(timeOff);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("TIME_OFF_DURING_SHIFT"));

    assertTrue(getScoreHolder().getHardScore() < 0);
  }

  @Test
  public void employeeHasNoTimeoffDuringShift() throws ParseException {

    Employee employee = new Employee().setId("1");

    Shift shift = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 11:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 11:30:00", dtf));

    TimeOff timeOff = new TimeOff().setEmployeeId("1")
        .setStart(LocalDateTime.parse("2018-01-19 00:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-20 00:00:00", dtf));

    ksession.insert(employee);
    ksession.insert(shift);
    ksession.insert(timeOff);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("TIME_OFF_DURING_SHIFT"));

    assertTrue(getScoreHolder().getHardScore() == 0);
  }

  @Test
  public void employeeHasNoTimeoffAtAll() throws ParseException {

    Employee employee = new Employee().setId("1");

    Shift shift = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 11:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 11:30:00", dtf));

    ksession.insert(employee);
    ksession.insert(shift);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("TIME_OFF_DURING_SHIFT"));

    assertTrue(getScoreHolder().getHardScore() == 0);
  }

}
