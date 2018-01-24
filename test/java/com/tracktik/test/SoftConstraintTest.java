package com.tracktik.test;

import com.tracktik.scheduler.domain.*;
import org.drools.core.ObjectFilter;
import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScoreHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringJUnit4ClassRunner.class)
public class SoftConstraintTest extends ConstraintRuleTestBase {

  @Test
  public void testEmployeeHasTwoSoftSkills() {

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

    Post post = new Post().setSoftSkills(postSkills);

    Shift shift = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setPost(post);

    ksession.insert(shift);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("has some requested soft skills"));

    assertEquals(100L, getScoreHolder().getSoftScore());
  }

  @Test
  public void testEmployeeHasNoSoftSkills() {

    Skill skill1 = new Skill().setId("skill1");
    Skill skill2 = new Skill().setId("skill2");
    Skill skill3 = new Skill().setId("skill3");

    List<Skill> employeeSkills = new ArrayList<>();

    Employee employee = new Employee().setId("1");
    employee.setSkills(employeeSkills);

    Set<Skill> postSkills = new HashSet<>();
    postSkills.add(skill1);
    postSkills.add(skill2);
    postSkills.add(skill3);

    Post post = new Post().setSoftSkills(postSkills);

    Shift shift = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setPost(post);

    ksession.insert(shift);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("has some requested soft skills"));

    assertEquals(0L, getScoreHolder().getSoftScore());
  }

  @Test
  public void testEmployeeHasSiteExperience() {

    Site site1 = new Site().setId("1");

    List<Site> sites = new ArrayList<>();
    sites.add(site1);

    Employee employee = new Employee().setId("1").setSiteExperience(sites);

    Shift shift = new Shift().setId("1").setEmployee(employee).setPost(new Post().setSite(site1));

    ksession.insert(shift);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("has site experience"));

    assertEquals(0L, getScoreHolder().getSoftScore());
  }

  @Test
  public void testEmployeeHasNoSiteExperience() {

    Site site1 = new Site().setId("1");
    Site site2 = new Site().setId("2");

    List<Site> sites = new ArrayList<>();
    sites.add(site2);

    Employee employee = new Employee().setId("1").setSiteExperience(sites);

    Shift shift = new Shift().setId("1").setEmployee(employee).setPost(new Post().setSite(site1));

    ksession.insert(shift);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("has site experience"));

    assertEquals(-100L, getScoreHolder().getSoftScore());
  }

  @Test
  public void testFarDistance() {

    Site site = new Site().setId("1").setLatitude(45.521849).setLongitude(-73.553196);

    Employee employee = new Employee().setId("2")
        .setLatitude(43.231556).setLongitude(-80.399524)
        .setCost(500L);

    Post post = new Post()
        .setSite(site).setId("3").setPayRate(5000L).setBillRate(7500L).setPayType(PayType.POST_RATE);
    Shift shift = new Shift().setId("4")
        .setTimeSlot(new TimeSlot().setStart(new Date()).setEnd(new Date()))
        .setEmployee(employee).setPost(post);

    ksession.insert(site);
    ksession.insert(employee);
    ksession.insert(post);
    ksession.insert(shift);

    ksession.fireAllRules(new RuleNameStartsWithAgendaFilter("workplace "));

    assertEquals(-15L, getScoreHolder().getSoftScore());

  }

  @Test
  public void testMediumDistance() {

    Site site = new Site().setId("1").setLatitude(45.521849).setLongitude(-73.553196);

    Employee employee = new Employee().setId("2")
        .setLatitude(45.404661).setLongitude(-74.080657)
        .setCost(500L);

    Post post = new Post()
        .setSite(site).setId("3").setPayRate(5000L).setBillRate(7500L).setPayType(PayType.POST_RATE);
    Shift shift = new Shift().setId("4")
        .setTimeSlot(new TimeSlot().setStart(new Date()).setEnd(new Date()))
        .setEmployee(employee).setPost(post);

    ksession.insert(site);
    ksession.insert(employee);
    ksession.insert(post);
    ksession.insert(shift);

    ksession.fireAllRules(new RuleNameStartsWithAgendaFilter("workplace "));

    assertEquals(-10L, getScoreHolder().getSoftScore());

  }


  @Test
  public void testFairlyCloseDistance() {

    Site site = new Site().setId("1").setLatitude(45.518193).setLongitude(-73.582305);

    Employee employee = new Employee().setId("2")
        .setLatitude(45.482001).setLongitude(-73.710184)
        .setCost(500L);

    Post post = new Post()
        .setSite(site).setId("3").setPayRate(5000L).setBillRate(7500L).setPayType(PayType.POST_RATE);
    Shift shift = new Shift().setId("4")
        .setTimeSlot(new TimeSlot().setStart(new Date()).setEnd(new Date()))
        .setEmployee(employee).setPost(post);

    ksession.insert(site);
    ksession.insert(employee);
    ksession.insert(post);
    ksession.insert(shift);

    ksession.fireAllRules(new RuleNameStartsWithAgendaFilter("workplace "));

    assertEquals(-5L, getScoreHolder().getSoftScore());

  }


  @Test
  public void testCloseDistance() {

    Site site = new Site().setId("1").setLatitude(45.518193).setLongitude(-73.582305);

    Employee employee = new Employee().setId("2")
        .setLatitude(45.518209).setLongitude(-73.577456)
        .setCost(500L);

    Post post = new Post()
        .setSite(site).setId("3").setPayRate(5000L).setBillRate(7500L).setPayType(PayType.POST_RATE);
    Shift shift = new Shift().setId("4")
        .setTimeSlot(new TimeSlot().setStart(new Date()).setEnd(new Date()))
        .setEmployee(employee).setPost(post);

    ksession.insert(site);
    ksession.insert(employee);
    ksession.insert(post);
    ksession.insert(shift);

    ksession.fireAllRules(new RuleNameStartsWithAgendaFilter("workplace "));

    assertEquals(5L, getScoreHolder().getSoftScore());

  }

}

//here 45.518193, -73.582305
//there 45.471168, -73.851290
