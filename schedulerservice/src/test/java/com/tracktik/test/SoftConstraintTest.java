package com.tracktik.test;

import com.tracktik.scheduler.domain.*;
import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.Assert.assertEquals;

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
        .setTimeSlot(new TimeSlot().setStart(new Date()).setEnd(new Date()))
        .setPost(post);

    ksession.insert(shift);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("HAS_SOME_SOFT_SKILLS"));

    assertEquals(100L, getScoreHolder().getSoftScore());
  }

  @Test
  public void testSoftSkillsEnabled() {

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
        .setTimeSlot(new TimeSlot().setStart(new Date()).setEnd(new Date()))
        .setPost(post);

    KeyValueFact keyValueFact = new KeyValueFact().setKey("SOFT_SKILL_ENABLED").setValue(true);

    ksession.insert(shift);
    ksession.insert(keyValueFact);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("HAS_SOME_SOFT_SKILLS"));

    assertEquals(100L, getScoreHolder().getSoftScore());
  }
  @Test
  public void testSoftSkillsEnabledWithMultiplier() {

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
        .setTimeSlot(new TimeSlot().setStart(new Date()).setEnd(new Date()))
        .setPost(post);

    KeyValueFact keyValueFactSoftSkill = new KeyValueFact().setKey("SOFT_SKILL_ENABLED").setValue(true);
    KeyValueFact keyValueFactMultiplier = new KeyValueFact().setKey("SOFT_SKILL_MUTLIPLIER").setValue(10);

    ksession.insert(shift);
    ksession.insert(keyValueFactSoftSkill);
    ksession.insert(keyValueFactMultiplier);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("HAS_SOME_SOFT_SKILLS_MULTIPLIER"));

    assertEquals(40L, getScoreHolder().getSoftScore());
  }

  @Test
  public void testHardSkillsAsSoftEnabledWithMultiplier() {

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
        .setTimeSlot(new TimeSlot().setStart(new Date()).setEnd(new Date()))
        .setPost(post);

    KeyValueFact keyValueFactHardSkill = new KeyValueFact().setKey("HARD_SKILL_ENABLED").setValue(true);
    KeyValueFact keyValueFactHardSkillAsSoft = new KeyValueFact().setKey("HARD_SKILL_IS_HARD").setValue(false);
    KeyValueFact keyValueFactMultiplier = new KeyValueFact().setKey("HARD_SKILL_TYPE_SOFT_MUTLIPLIER").setValue(100);

    ksession.insert(shift);
    ksession.insert(keyValueFactHardSkill);
    ksession.insert(keyValueFactMultiplier);
    ksession.insert(keyValueFactHardSkillAsSoft);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("EMPLOYEE_MUST_HAVE_HARD_SKILLS_AS_SOFT"));

    assertEquals(200L, getScoreHolder().getSoftScore());
  }

  @Test
  public void testSoftSkillsNotEnabled() {

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
        .setTimeSlot(new TimeSlot().setStart(new Date()).setEnd(new Date()))
        .setPost(post);

    KeyValueFact keyValueFact = new KeyValueFact().setKey("SOFT_SKILL_ENABLED").setValue(false);

    ksession.insert(shift);
    ksession.insert(keyValueFact);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("HAS_SOME_SOFT_SKILLS"));

    assertEquals(0L, getScoreHolder().getSoftScore());
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
        .setTimeSlot(new TimeSlot().setStart(new Date()).setEnd(new Date()))
        .setPost(post);

    ksession.insert(shift);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("HAS_SOME_SOFT_SKILLS"));

    assertEquals(0L, getScoreHolder().getSoftScore());
  }

  @Test
  public void testEmployeeHasSiteExperience() {

    Site site1 = new Site().setId("1");

    List<Site> sites = new ArrayList<>();
    sites.add(site1);

    Employee employee = new Employee().setId("1").setSiteExperience(sites);

    Shift shift = new Shift().setId("1").setEmployee(employee).setPost(new Post().setSite(site1))
        .setTimeSlot(new TimeSlot().setStart(new Date()).setEnd(new Date()));

    ksession.insert(shift);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("HAS_SITE_EXPERIENCE"));

    assertEquals(0L, getScoreHolder().getSoftScore());
  }

  @Test
  public void testEmployeeHasNoSiteExperience() {

    Site site1 = new Site().setId("1");
    Site site2 = new Site().setId("2");

    List<Site> sites = new ArrayList<>();
    sites.add(site2);

    Employee employee = new Employee().setId("1").setSiteExperience(sites);

    Shift shift = new Shift().setId("1").setEmployee(employee).setPost(new Post().setSite(site1))
        .setTimeSlot(new TimeSlot().setStart(new Date()).setEnd(new Date()));

    ksession.insert(shift);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("HAS_SITE_EXPERIENCE"));

    assertEquals(-100L, getScoreHolder().getSoftScore());
  }

  @Test
  public void testEmployeeHasPostExperience() {

    Site site1 = new Site().setId("1");

    Post post1 = new Post();
    Post post2 = new Post();

    List<Post> posts = new ArrayList<>();
    posts.add(post1);
    posts.add(post2);

    List<Site> sites = new ArrayList<>();
    sites.add(site1);

    Employee employee = new Employee()
        .setId("1").setSiteExperience(sites).setPostExperience(posts);

    Shift shift = new Shift().setId("1").setEmployee(employee).setPost(post1)
        .setTimeSlot(new TimeSlot().setStart(new Date()).setEnd(new Date()));

    ksession.insert(shift);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("HAS_POST_EXPERIENCE"));

    assertEquals(0L, getScoreHolder().getSoftScore());
  }

  /* Not implemented yet
  @Test
  public void testEmployeeHasNoPostExperience() {


    Site site1 = new Site().setId("1");
    List<Site> sites = new ArrayList<>();
    sites.add(site1);

    Post post1 = new Post().setId("1").setSite(site1);
    Post post2 = new Post().setId("2").setSite(new Site().setId("2"));

    List<Post> posts = new ArrayList<>();
    posts.add(post2);

    Employee employee = new Employee()
        .setId("1").setSiteExperience(sites).setPostExperience(posts);

    Shift shift = new Shift().setId("1").setEmployee(employee)
        .setTimeSlot(new TimeSlot().setStart(new Date())
        .setEnd(new Date())).setPost(post1);

    ksession.insert(shift);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("HAS_POST_EXPERIENCE"));

    assertEquals(-100L, getScoreHolder().getSoftScore());
  } */

  @Test
  public void testEmployeeCloseToSiteDistance() {

    Site site = new Site().setId("1").setLatitude(45.518193).setLongitude(-73.582305);

    Employee employee = new Employee().setId("2")
        .setLatitude(45.482001).setLongitude(-73.710184)
        .setCost(500L);

    Post post = new Post()
        .setSite(site).setId("3").setPayRate(5000L).setBillRate(7500L).setPayType(PayType.POST_RATE);
    Shift shift = new Shift().setId("4")
        .setTimeSlot(new TimeSlot().setStart(new Date()).setEnd(new Date()))
        .setEmployee(employee).setPost(post);

    EmployeeSiteDistance esd = new EmployeeSiteDistance().setDistance(1L).setEmployeeId(employee.getId()).setSiteId(site.getId());

    ksession.insert(new KeyValueFact().setKey("CLOSE_FROM_SITE_ENABLED").setValue(true));
    ksession.insert(new KeyValueFact().setKey("CLOSE_FROM_SITE_DEFINITION").setValue(10L));
    ksession.insert(new KeyValueFact().setKey("CLOSE_FROM_SITE_IMPACT").setValue(5L));

    ksession.insert(new KeyValueFact().setKey("FAIRLY_FAR_FROM_SITE_ENABLED").setValue(true));
    ksession.insert(new KeyValueFact().setKey("FAIRLY_FAR_FROM_SITE_DEFINITION").setValue(25L));
    ksession.insert(new KeyValueFact().setKey("FAIRLY_FROM_FROM_SITE_IMPACT").setValue(-10L));

    ksession.insert(new KeyValueFact().setKey("FAR_FROM_SITE_ENABLED").setValue(true));
    ksession.insert(new KeyValueFact().setKey("FAR_FROM_SITE_DEFINITION").setValue(50L));
    ksession.insert(new KeyValueFact().setKey("FAR_FROM_SITE_IMPACT").setValue(-20L));

    ksession.insert(site);
    ksession.insert(employee);
    ksession.insert(post);
    ksession.insert(shift);
    ksession.insert(esd);

    ksession.fireAllRules(new RuleNameStartsWithAgendaFilter("WORKPLACE_"));

    assertEquals(5L, getScoreHolder().getSoftScore());

  }

  @Test
  public void testEmployeeMediumDistanctToSiteDistance() {

    Site site = new Site().setId("1").setLatitude(45.518193).setLongitude(-73.582305);

    Employee employee = new Employee().setId("2")
        .setLatitude(45.482001).setLongitude(-73.710184)
        .setCost(500L);

    Post post = new Post()
        .setSite(site).setId("3").setPayRate(5000L).setBillRate(7500L).setPayType(PayType.POST_RATE);
    Shift shift = new Shift().setId("4")
        .setTimeSlot(new TimeSlot().setStart(new Date()).setEnd(new Date()))
        .setEmployee(employee).setPost(post);

    EmployeeSiteDistance esd = new EmployeeSiteDistance().setDistance(20L).setEmployeeId(employee.getId()).setSiteId(site.getId());

    ksession.insert(new KeyValueFact().setKey("CLOSE_FROM_SITE_ENABLED").setValue(true));
    ksession.insert(new KeyValueFact().setKey("CLOSE_FROM_SITE_DEFINITION").setValue(10L));
    ksession.insert(new KeyValueFact().setKey("CLOSE_FROM_SITE_IMPACT").setValue(5L));

    ksession.insert(new KeyValueFact().setKey("FAIRLY_FAR_FROM_SITE_ENABLED").setValue(true));
    ksession.insert(new KeyValueFact().setKey("FAIRLY_FAR_FROM_SITE_DEFINITION").setValue(25L));
    ksession.insert(new KeyValueFact().setKey("FAIRLY_FROM_FROM_SITE_IMPACT").setValue(-10L));

    ksession.insert(new KeyValueFact().setKey("FAR_FROM_SITE_ENABLED").setValue(true));
    ksession.insert(new KeyValueFact().setKey("FAR_FROM_SITE_DEFINITION").setValue(50L));
    ksession.insert(new KeyValueFact().setKey("FAR_FROM_SITE_IMPACT").setValue(-20L));

    ksession.insert(site);
    ksession.insert(employee);
    ksession.insert(post);
    ksession.insert(shift);
    ksession.insert(esd);

    ksession.fireAllRules(new RuleNameStartsWithAgendaFilter("WORKPLACE_"));

    assertEquals(0L, getScoreHolder().getSoftScore());

  }

  @Test
  public void testEmployeeFairlyFarToSiteDistance() {

    Site site = new Site().setId("1").setLatitude(45.518193).setLongitude(-73.582305);

    Employee employee = new Employee().setId("2")
        .setLatitude(45.482001).setLongitude(-73.710184)
        .setCost(500L);

    Post post = new Post()
        .setSite(site).setId("3").setPayRate(5000L).setBillRate(7500L).setPayType(PayType.POST_RATE);
    Shift shift = new Shift().setId("4")
        .setTimeSlot(new TimeSlot().setStart(new Date()).setEnd(new Date()))
        .setEmployee(employee).setPost(post);

    EmployeeSiteDistance esd = new EmployeeSiteDistance().setDistance(30L).setEmployeeId(employee.getId()).setSiteId(site.getId());

    ksession.insert(new KeyValueFact().setKey("CLOSE_FROM_SITE_ENABLED").setValue(true));
    ksession.insert(new KeyValueFact().setKey("CLOSE_FROM_SITE_DEFINITION").setValue(10L));
    ksession.insert(new KeyValueFact().setKey("CLOSE_FROM_SITE_IMPACT").setValue(5L));

    ksession.insert(new KeyValueFact().setKey("FAIRLY_FAR_FROM_SITE_ENABLED").setValue(true));
    ksession.insert(new KeyValueFact().setKey("FAIRLY_FAR_FROM_SITE_DEFINITION").setValue(25L));
    ksession.insert(new KeyValueFact().setKey("FAIRLY_FROM_FROM_SITE_IMPACT").setValue(-10L));

    ksession.insert(new KeyValueFact().setKey("FAR_FROM_SITE_ENABLED").setValue(true));
    ksession.insert(new KeyValueFact().setKey("FAR_FROM_SITE_DEFINITION").setValue(50L));
    ksession.insert(new KeyValueFact().setKey("FAR_FROM_SITE_IMPACT").setValue(-20L));

    ksession.insert(site);
    ksession.insert(employee);
    ksession.insert(post);
    ksession.insert(shift);
    ksession.insert(esd);

    ksession.fireAllRules(new RuleNameStartsWithAgendaFilter("WORKPLACE_"));

    assertEquals(-10L, getScoreHolder().getSoftScore());

  }

  @Test
  public void testEmployeeFarToSiteDistance() {

    Site site = new Site().setId("1").setLatitude(45.518193).setLongitude(-73.582305);

    Employee employee = new Employee().setId("2")
        .setLatitude(45.482001).setLongitude(-73.710184)
        .setCost(500L);

    Post post = new Post()
        .setSite(site).setId("3").setPayRate(5000L).setBillRate(7500L).setPayType(PayType.POST_RATE);
    Shift shift = new Shift().setId("4")
        .setTimeSlot(new TimeSlot().setStart(new Date()).setEnd(new Date()))
        .setEmployee(employee).setPost(post);

    EmployeeSiteDistance esd = new EmployeeSiteDistance().setDistance(60L).setEmployeeId(employee.getId()).setSiteId(site.getId());

    ksession.insert(new KeyValueFact().setKey("CLOSE_FROM_SITE_ENABLED").setValue(true));
    ksession.insert(new KeyValueFact().setKey("CLOSE_FROM_SITE_DEFINITION").setValue(10L));
    ksession.insert(new KeyValueFact().setKey("CLOSE_FROM_SITE_IMPACT").setValue(5L));

    ksession.insert(new KeyValueFact().setKey("FAIRLY_FAR_FROM_SITE_ENABLED").setValue(true));
    ksession.insert(new KeyValueFact().setKey("FAIRLY_FAR_FROM_SITE_DEFINITION").setValue(25L));
    ksession.insert(new KeyValueFact().setKey("FAIRLY_FROM_FROM_SITE_IMPACT").setValue(-10L));

    ksession.insert(new KeyValueFact().setKey("FAR_FROM_SITE_ENABLED").setValue(true));
    ksession.insert(new KeyValueFact().setKey("FAR_FROM_SITE_DEFINITION").setValue(50L));
    ksession.insert(new KeyValueFact().setKey("FAR_FROM_SITE_IMPACT").setValue(-20L));

    ksession.insert(site);
    ksession.insert(employee);
    ksession.insert(post);
    ksession.insert(shift);
    ksession.insert(esd);

    ksession.fireAllRules(new RuleNameStartsWithAgendaFilter("WORKPLACE_"));

    assertEquals(-30L, getScoreHolder().getSoftScore());

  }

  @Test
  public void testEmployeeCoordinatesMissing() {

    Site site = new Site().setId("1").setLatitude(45.518193).setLongitude(-73.582305);

    Employee employee = new Employee().setId("2")
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

    ksession.fireAllRules(new RuleNameStartsWithAgendaFilter("WORKPLACE_"));

    assertEquals(0L, getScoreHolder().getSoftScore());

  }

  @Test
  public void testSiteCoordinatesMissing() {

    Site site = new Site().setId("1");

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

    ksession.fireAllRules(new RuleNameStartsWithAgendaFilter("WORKPLACE_"));

    assertEquals(0L, getScoreHolder().getSoftScore());

  }

  @Test
  public void testEmployeeIsNotBannedFromSite() {

    Site site = new Site().setId("1");

    Employee employee = new Employee().setId("2");

    Post post = new Post()
        .setSite(site).setId("3");
    Shift shift = new Shift().setId("4")
        .setTimeSlot(new TimeSlot().setStart(new Date()).setEnd(new Date()))
        .setEmployee(employee).setPost(post);

    SiteBan ban = new SiteBan().setEmployeeId("200").setSiteId("1");

    ksession.insert(site);
    ksession.insert(employee);
    ksession.insert(post);
    ksession.insert(shift);
    ksession.insert(ban);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("employee is banned from a site"));

    assertEquals(0L, getScoreHolder().getHardScore());

  }

  @Test
  public void testEmployeeHasSeniority() {

    Site site = new Site().setId("1");

    Employee employee = new Employee().setId("2").setSeniority(57);

    Post post = new Post().setSite(site).setId("3");
    Shift shift = new Shift().setId("4").setEmployee(employee).setPost(post)
        .setTimeSlot(new TimeSlot().setStart(new Date()).setEnd(new Date()));

    ksession.insert(site);
    ksession.insert(employee);
    ksession.insert(post);
    ksession.insert(shift);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("EMPLOYEE_HAS_SENIORITY"));

    assertEquals(57L, getScoreHolder().getSoftScore());

  }

  @Test
  public void testEmployeeHasNoSeniority() {

    Site site = new Site().setId("1");

    Employee employee = new Employee().setId("2");

    Post post = new Post().setSite(site).setId("3");
    Shift shift = new Shift().setId("4").setEmployee(employee).setPost(post)
        .setTimeSlot(new TimeSlot().setStart(new Date()).setEnd(new Date()));

    ksession.insert(site);
    ksession.insert(employee);
    ksession.insert(post);
    ksession.insert(shift);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("EMPLOYEE_HAS_SENIORITY"));

    assertEquals(0L, getScoreHolder().getSoftScore());

  }

  @Test
  public void employeeHasNoWorkPreference() throws ParseException {

    Employee employee = new Employee().setId("1");

    Shift shift = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setTimeSlot(new TimeSlot("2018-01-17 11:00:00", "2018-01-17 11:30:00"));

    ksession.insert(employee);
    ksession.insert(shift);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("employee prefers not to work shift"));

    assertEquals(0L, getScoreHolder().getSoftScore());
  }
  @Test
  public void employeePrefersNotWorkShiftNoOverlap() throws ParseException {

    Employee employee = new Employee().setId("1");

    //Shift is on Wednesday
    Shift shift = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setTimeSlot(new TimeSlot("2018-01-17 11:00:00", "2018-01-17 11:30:00"));

    EmployeeAvailability availability = new EmployeeAvailability()
        .setEmployeeId("1").setType(AvailabilityType.NO)
        .setDayOfWeek(DayOfWeek.FRIDAY)
        .setStartTime(LocalTime.MIDNIGHT.plus(10L, ChronoUnit.HOURS))
        .setEndTime(LocalTime.MIDNIGHT.plus(12L, ChronoUnit.HOURS));

    ksession.insert(employee);
    ksession.insert(shift);
    ksession.insert(availability);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("employee prefers not to work shift"));

    assertEquals(0L, getScoreHolder().getSoftScore());
  }
/*
  @Test
  public void employeePrefersNotWorkShiftOverlap() throws ParseException {

    Employee employee = new Employee().setId("1");

    //Shift is on Wednesday
    Shift shift = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setTimeSlot(new TimeSlot("2018-01-17 11:00:00", "2018-01-17 11:30:00"));

    EmployeeAvailability availability = new EmployeeAvailability()
        .setEmployeeId("1").setType(AvailabilityType.NO)
        .setDayOfWeek(DayOfWeek.WEDNESDAY)
        .setStartTime(LocalTime.MIDNIGHT.plus(0L, ChronoUnit.HOURS))
        .setEndTime(LocalTime.MIDNIGHT.plus(12L, ChronoUnit.HOURS));

    ksession.insert(employee);
    ksession.insert(shift);
    ksession.insert(availability);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("employee prefers not to work shift"));

    assertEquals(-10L, getScoreHolder().getSoftScore());
  }
*/
  @Test
  public void employeeMayBeAvailableWorkShiftNoOverlap() throws ParseException {

    Employee employee = new Employee().setId("1");

    //Shift is on Wednesday
    Shift shift = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setTimeSlot(new TimeSlot("2018-01-17 11:00:00", "2018-01-17 11:30:00"));

    EmployeeAvailability availability = new EmployeeAvailability()
        .setEmployeeId("1").setType(AvailabilityType.MAYBE)
        .setDayOfWeek(DayOfWeek.FRIDAY)
        .setStartTime(LocalTime.MIDNIGHT.plus(10L, ChronoUnit.HOURS))
        .setEndTime(LocalTime.MIDNIGHT.plus(12L, ChronoUnit.HOURS));

    ksession.insert(employee);
    ksession.insert(shift);
    ksession.insert(availability);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("employee may be available to work shift"));

    assertEquals(0L, getScoreHolder().getSoftScore());
  }

  /*
  @Test
  public void employeeMayBeAvailableWorkShiftOverlap() throws ParseException {

    Employee employee = new Employee().setId("1");

    //Shift is on Wednesday
    Shift shift = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setTimeSlot(new TimeSlot("2018-01-17 11:00:00", "2018-01-17 11:30:00"));

    EmployeeAvailability availability = new EmployeeAvailability()
        .setEmployeeId("1").setType(AvailabilityType.MAYBE)
        .setDayOfWeek(DayOfWeek.WEDNESDAY)
        .setStartTime(LocalTime.MIDNIGHT.plus(0L, ChronoUnit.HOURS))
        .setEndTime(LocalTime.MIDNIGHT.plus(12L, ChronoUnit.HOURS));

    ksession.insert(employee);
    ksession.insert(shift);
    ksession.insert(availability);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("employee may be available to work shift"));

    assertEquals(-5L, getScoreHolder().getSoftScore());
  }
  */

}

