package com.tracktik.test;

import com.tracktik.scheduler.domain.*;
import org.drools.core.base.RuleNameEndsWithAgendaFilter;
import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.drools.core.base.RuleNameMatchesAgendaFilter;
import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.event.rule.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
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
        .setStart(LocalDateTime.now())
        .setEnd(LocalDateTime.now())
        .setPost(post);

    ksession.insert(shift);
    ksession.insert(employee);
    ksession.fireAllRules(new RuleNameMatchesAgendaFilter(".*HAS_SOME_SOFT_SKILLS.*"));

    assertEquals(100L, getScoreHolder().getSoftScore());
  }
  @Test
  public void testEmployeeWithMultiplierHasTwoSoftSkills() {

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
        .setStart(LocalDateTime.now())
        .setEnd(LocalDateTime.now())
        .setPost(post);
    EmployeeConstraintMultiplier employeeConstraintMultiplier = new EmployeeConstraintMultiplier()
        .setEmployeeId("1").setName("HAS_SOME_SOFT_SKILLS").setMultiplier(2.0);

    ksession.insert(employeeConstraintMultiplier);
    ksession.insert(shift);
    ksession.insert(employee);
    ksession.fireAllRules(new RuleNameMatchesAgendaFilter(".*HAS_SOME_SOFT_SKILLS.*"));

    assertEquals(200L, getScoreHolder().getSoftScore());
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
        .setStart(LocalDateTime.now())
        .setEnd(LocalDateTime.now())
        .setPost(post);

    KeyValueFact keyValueFact = new KeyValueFact().setKey("SOFT_SKILL_ENABLED").setValue(true);

    ksession.insert(shift);
    ksession.insert(employee);
    ksession.insert(keyValueFact);

    ksession.fireAllRules(new RuleNameEndsWithAgendaFilter("HAS_SOME_SOFT_SKILLS"));

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
        .setStart(LocalDateTime.now())
        .setEnd(LocalDateTime.now())
        .setPost(post);

    KeyValueFact keyValueFactSoftSkill = new KeyValueFact().setKey("SOFT_SKILL_ENABLED").setValue(true);
    KeyValueFact keyValueFactMultiplier = new KeyValueFact().setKey("SOFT_SKILL_MUTLIPLIER").setValue(10);

    ksession.insert(employee);
    ksession.insert(shift);
    ksession.insert(keyValueFactSoftSkill);
    ksession.insert(keyValueFactMultiplier);

    ksession.fireAllRules(new RuleNameEndsWithAgendaFilter("HAS_SOME_SOFT_SKILLS"));

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
        .setStart(LocalDateTime.now())
        .setEnd(LocalDateTime.now())
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
        .setStart(LocalDateTime.now())
        .setEnd(LocalDateTime.now())
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
        .setStart(LocalDateTime.now())
        .setEnd(LocalDateTime.now())
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
        .setStart(LocalDateTime.now())
        .setEnd(LocalDateTime.now());

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
        .setStart(LocalDateTime.now())
        .setEnd(LocalDateTime.now());

    ksession.insert(employee);
    ksession.insert(shift);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter(".*HAS_SITE_EXPERIENCE.*"));

    assertEquals(-100L, getScoreHolder().getSoftScore());
  }

  @Test
  public void testEmployeeWithMultiplierHasNoSiteExperience() {

    Site site1 = new Site().setId("1");
    Site site2 = new Site().setId("2");

    List<Site> sites = new ArrayList<>();
    sites.add(site2);

    Employee employee = new Employee().setId("1").setSiteExperience(sites);

    Shift shift = new Shift().setId("1").setEmployee(employee).setPost(new Post().setSite(site1))
        .setStart(LocalDateTime.now())
        .setEnd(LocalDateTime.now());
    EmployeeConstraintMultiplier employeeConstraintMultiplier = new EmployeeConstraintMultiplier()
        .setEmployeeId("1").setName("HAS_SITE_EXPERIENCE").setMultiplier(2.0);

    ksession.insert(employeeConstraintMultiplier);
    ksession.insert(employee);
    ksession.insert(shift);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter(".*HAS_SITE_EXPERIENCE.*"));

    assertEquals(-200L, getScoreHolder().getSoftScore());
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
        .setStart(LocalDateTime.now())
        .setEnd(LocalDateTime.now());

    ksession.insert(shift);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("HAS_POST_EXPERIENCE"));

    assertEquals(0L, getScoreHolder().getSoftScore());
  }

  @Test
  public void testEmployeeCloseToSiteDistance() {

    Site site = new Site().setId("1").setLatitude(45.518193).setLongitude(-73.582305);

    Employee employee = new Employee().setId("2")
        .setLatitude(45.482001).setLongitude(-73.710184)
        .setCost(500L);

    Post post = new Post()
        .setSite(site).setId("3").setPayRate(5000L).setBillRate(7500L).setPayType(PayType.POST_RATE);
    Shift shift = new Shift().setId("4")
        .setStart(LocalDateTime.now())
        .setEnd(LocalDateTime.now())
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

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter(".*WORKPLACE_.*"));

    assertEquals(5L, getScoreHolder().getSoftScore());

  }
  @Test
  public void testEmployeeWithMultiplierCloseToSiteDistance() {

    Site site = new Site().setId("1").setLatitude(45.518193).setLongitude(-73.582305);

    Employee employee = new Employee().setId("2")
        .setLatitude(45.482001).setLongitude(-73.710184)
        .setCost(500L);

    Post post = new Post()
        .setSite(site).setId("3").setPayRate(5000L).setBillRate(7500L).setPayType(PayType.POST_RATE);
    Shift shift = new Shift().setId("4")
        .setStart(LocalDateTime.now())
        .setEnd(LocalDateTime.now())
        .setEmployee(employee).setPost(post);

    EmployeeSiteDistance esd = new EmployeeSiteDistance().setDistance(1L).setEmployeeId(employee.getId()).setSiteId(site.getId());
    EmployeeConstraintMultiplier multiplier = new EmployeeConstraintMultiplier().setEmployeeId("2").setName("WORKPLACE_CLOSE").setMultiplier(1.5);

    ksession.insert(multiplier);
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

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter(".*WORKPLACE_.*"));

    assertEquals(7L, getScoreHolder().getSoftScore());

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
        .setStart(LocalDateTime.now())
        .setEnd(LocalDateTime.now())
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
        .setStart(LocalDateTime.now())
        .setEnd(LocalDateTime.now())
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

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter(".*WORKPLACE_.*"));

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
        .setStart(LocalDateTime.now())
        .setEnd(LocalDateTime.now())
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

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter(".*WORKPLACE_.*"));

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
        .setStart(LocalDateTime.now())
        .setEnd(LocalDateTime.now())
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
        .setStart(LocalDateTime.now())
        .setEnd(LocalDateTime.now())
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
        .setStart(LocalDateTime.now())
        .setEnd(LocalDateTime.now())
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
        .setStart(LocalDateTime.now())
        .setEnd(LocalDateTime.now());

    ksession.insert(site);
    ksession.insert(employee);
    ksession.insert(post);
    ksession.insert(shift);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter(".*EMPLOYEE_HAS_SENIORITY.*"));

    assertEquals(57L, getScoreHolder().getSoftScore());

  }

  @Test
  public void testEmployeeWithMultiplierHasSeniority() {

    Site site = new Site().setId("1");

    Employee employee = new Employee().setId("2").setSeniority(57);

    Post post = new Post().setSite(site).setId("3");
    Shift shift = new Shift().setId("4").setEmployee(employee).setPost(post)
        .setStart(LocalDateTime.now())
        .setEnd(LocalDateTime.now());

    EmployeeConstraintMultiplier employeeConstraintMultiplier = new EmployeeConstraintMultiplier()
        .setEmployeeId("2").setName("EMPLOYEE_HAS_SENIORITY").setMultiplier(2.0);

    ksession.insert(site);
    ksession.insert(employee);
    ksession.insert(post);
    ksession.insert(shift);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter(".*EMPLOYEE_HAS_SENIORITY.*"));

    assertEquals(57L, getScoreHolder().getSoftScore());

  }

  @Test
  public void testEmployeeHasNoSeniority() {

    Site site = new Site().setId("1");

    Employee employee = new Employee().setId("2");

    Post post = new Post().setSite(site).setId("3");
    Shift shift = new Shift().setId("4").setEmployee(employee).setPost(post)
        .setStart(LocalDateTime.now())
        .setEnd(LocalDateTime.now());

    ksession.insert(site);
    ksession.insert(employee);
    ksession.insert(post);
    ksession.insert(shift);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("EMPLOYEE_HAS_SENIORITY"));

    assertEquals(0L, getScoreHolder().getSoftScore());

  }

  @Test
  public void employeeHasLessThanExpectedHours() {

    Employee employee = new Employee().setId("1").setPreferredHours(5L);

    Shift shift1 = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 11:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 12:00:00", dtf));

    Shift shift2 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 13:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 14:00:00", dtf));

    KeyValueFact ruleEnabled = new KeyValueFact().setKey("LESS_THAN_EXPECTED_ENABLED").setValue(true);
    KeyValueFact impact = new KeyValueFact().setKey("LESS_THAN_EXPECTED_HOURS_IMPACT").setValue(5L);

    ksession.insert(ruleEnabled);
    ksession.insert(impact);
    ksession.insert(employee);
    ksession.insert(shift1);
    ksession.insert(shift2);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter(".*LESS_THAN_EXPECTED_HOURS.*"));

    assertEquals(-300L, getScoreHolder().getSoftScore());
  }

  @Test
  public void employeeWithMultiplierHasLessThanExpectedHours() {

    Employee employee = new Employee().setId("1").setPreferredHours(5L);

    Shift shift1 = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 11:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 12:00:00", dtf));

    Shift shift2 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 13:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 14:00:00", dtf));

    EmployeeConstraintMultiplier employeeConstraintMultiplier = new EmployeeConstraintMultiplier()
        .setEmployeeId("1").setName("LESS_THAN_EXPECTED_HOURS").setMultiplier(2.0);

    KeyValueFact ruleEnabled = new KeyValueFact().setKey("LESS_THAN_EXPECTED_ENABLED").setValue(true);
    KeyValueFact impact = new KeyValueFact().setKey("LESS_THAN_EXPECTED_HOURS_IMPACT").setValue(5L);

    ksession.insert(ruleEnabled);
    ksession.insert(impact);
    ksession.insert(employee);
    ksession.insert(employeeConstraintMultiplier);
    ksession.insert(shift1);
    ksession.insert(shift2);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter(".*LESS_THAN_EXPECTED_HOURS.*"));

    assertEquals(-600L, getScoreHolder().getSoftScore());
  }

  @Test
  public void employeeHasMoreThanExpectedHours() {

    Employee employee = new Employee().setId("1").setPreferredHours(5L);

    Shift shift1 = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 01:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 06:00:00", dtf));

    Shift shift2 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 07:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 12:00:00", dtf));

    KeyValueFact ruleEnabled = new KeyValueFact().setKey("MORE_THAN_EXPECTED_ENABLED").setValue(true);
    KeyValueFact impact = new KeyValueFact().setKey("MORE_THAN_EXPECTED_HOURS_IMPACT").setValue(5L);

    ksession.insert(ruleEnabled);
    ksession.insert(impact);
    ksession.insert(employee);
    ksession.insert(shift1);
    ksession.insert(shift2);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter(".*MORE_THAN_EXPECTED_HOURS.*"));

    assertEquals(-500L, getScoreHolder().getSoftScore());
  }

  @Test
  public void employeeWithMultiplierHasMoreThanExpectedHours() {

    Employee employee = new Employee().setId("1").setPreferredHours(5L);

    Shift shift1 = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 01:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 06:00:00", dtf));

    Shift shift2 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 07:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 12:00:00", dtf));

    EmployeeConstraintMultiplier employeeConstraintMultiplier = new EmployeeConstraintMultiplier()
        .setEmployeeId("1").setName("MORE_THAN_EXPECTED_HOURS").setMultiplier(2.0);

    KeyValueFact ruleEnabled = new KeyValueFact().setKey("MORE_THAN_EXPECTED_ENABLED").setValue(true);
    KeyValueFact impact = new KeyValueFact().setKey("MORE_THAN_EXPECTED_HOURS_IMPACT").setValue(5L);

    ksession.insert(ruleEnabled);
    ksession.insert(impact);
    ksession.insert(employeeConstraintMultiplier);
    ksession.insert(employee);
    ksession.insert(shift1);
    ksession.insert(shift2);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter(".*MORE_THAN_EXPECTED_HOURS.*"));

    assertEquals(-1000L, getScoreHolder().getSoftScore());
  }

  @Test
  public void employeeHasNoWorkPreference() {

    Employee employee = new Employee().setId("1");

    Shift shift = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 11:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 11:30:00", dtf));

    ksession.insert(employee);
    ksession.insert(shift);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("employee prefers not to work shift"));

    assertEquals(0L, getScoreHolder().getSoftScore());
  }

  @Test
  public void employeePrefersNotWorkShiftNoOverlap() {

    Employee employee = new Employee().setId("1");

    //Shift is on Wednesday
    Shift shift = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 11:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 11:30:00", dtf));

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

  @Test
  public void employeePrefersNotWorkShiftOverlap() {

    Employee employee = new Employee().setId("1");

    //Shift is on Wednesday
    Shift shift = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 11:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 11:30:00", dtf));

    EmployeeAvailability availability = new EmployeeAvailability()
        .setEmployeeId("1").setType(AvailabilityType.NO)
        .setDayOfWeek(DayOfWeek.WEDNESDAY)
        .setStartTime(LocalTime.MIDNIGHT.plus(0L, ChronoUnit.HOURS))
        .setEndTime(LocalTime.MIDNIGHT.plus(12L, ChronoUnit.HOURS));

    KeyValueFact keyValueFact = new KeyValueFact().setKey("AVAILABLE_PREF_NO_IMPACT").setValue(-5);

    ksession.insert(keyValueFact);
    ksession.insert(employee);
    ksession.insert(shift);
    ksession.insert(availability);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter(".*AVAILABILITY_NO.*"));

    assertEquals(-5L, getScoreHolder().getSoftScore());
  }

  @Test
  public void employeeWithMultiplierPrefersNotWorkShiftOverlap() {

    Employee employee = new Employee().setId("1");

    //Shift is on Wednesday
    Shift shift = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 11:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 11:30:00", dtf));

    EmployeeAvailability availability = new EmployeeAvailability()
        .setEmployeeId("1").setType(AvailabilityType.NO)
        .setDayOfWeek(DayOfWeek.WEDNESDAY)
        .setStartTime(LocalTime.MIDNIGHT.plus(0L, ChronoUnit.HOURS))
        .setEndTime(LocalTime.MIDNIGHT.plus(12L, ChronoUnit.HOURS));

    KeyValueFact keyValueFact = new KeyValueFact().setKey("AVAILABLE_PREF_NO_IMPACT").setValue(-5);
    EmployeeConstraintMultiplier employeeConstraintMultiplier = new EmployeeConstraintMultiplier()
        .setEmployeeId("1").setName("AVAILABILITY_NO").setMultiplier(2.0);

    ksession.insert(employeeConstraintMultiplier);
    ksession.insert(keyValueFact);
    ksession.insert(employee);
    ksession.insert(shift);
    ksession.insert(availability);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter(".*AVAILABILITY_NO.*"));

    assertEquals(-10L, getScoreHolder().getSoftScore());
  }

  @Test
  public void employeeMayBeAvailableWorkShiftNoOverlap() {

    Employee employee = new Employee().setId("1");

    //Shift is on Wednesday
    Shift shift = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 11:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 11:30:00", dtf));

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

  @Test
  public void testConsecutiveWorkDays() {

    Employee employee = new Employee().setId("1");

    Shift shift1 = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 11:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 11:30:00", dtf));


    Shift shift2 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-18 11:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-18 11:30:00", dtf));

    ksession.insert(employee);
    ksession.insert(shift1);
    ksession.insert(shift2);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("CONSECUTIVE_WORK_DAYS"));

    assertEquals(100L, getScoreHolder().getSoftScore());
  }
  @Test
  public void testNonConsecutiveWorkDays() {

    Employee employee = new Employee().setId("1");

    Shift shift1 = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-16 11:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-16 11:30:00", dtf));


    Shift shift2 = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-18 11:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-18 11:30:00", dtf));

    ksession.insert(employee);
    ksession.insert(shift1);
    ksession.insert(shift2);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("CONSECUTIVE_WORK_DAYS"));

    assertEquals(0L, getScoreHolder().getSoftScore());
  }

  @Test
  public void employeeMayBeAvailableWorkShiftOverlap() {

    Employee employee = new Employee().setId("1");

    //Shift is on Wednesday
    Shift shift = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 11:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 11:30:00", dtf));

    EmployeeAvailability availability = new EmployeeAvailability()
        .setEmployeeId("1").setType(AvailabilityType.MAYBE)
        .setDayOfWeek(DayOfWeek.WEDNESDAY)
        .setStartTime(LocalTime.MIDNIGHT.plus(0L, ChronoUnit.HOURS))
        .setEndTime(LocalTime.MIDNIGHT.plus(12L, ChronoUnit.HOURS));

    KeyValueFact keyValueFact = new KeyValueFact().setKey("AVAILABLE_PREF_MAYBE_IMPACT").setValue(-5L);

    ksession.insert(keyValueFact);
    ksession.insert(employee);
    ksession.insert(shift);
    ksession.insert(availability);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter(".*AVAILABILITY_MAYBE.*"));

    assertEquals(-5L, getScoreHolder().getSoftScore());
  }


  @Test
  public void employeeWithMultiplierMayBeAvailableWorkShiftOverlap() {

    Employee employee = new Employee().setId("1");

    //Shift is on Wednesday
    Shift shift = new Shift()
        .setId("2")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 11:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 11:30:00", dtf));

    EmployeeAvailability availability = new EmployeeAvailability()
        .setEmployeeId("1").setType(AvailabilityType.MAYBE)
        .setDayOfWeek(DayOfWeek.WEDNESDAY)
        .setStartTime(LocalTime.MIDNIGHT.plus(0L, ChronoUnit.HOURS))
        .setEndTime(LocalTime.MIDNIGHT.plus(12L, ChronoUnit.HOURS));

    KeyValueFact keyValueFact = new KeyValueFact().setKey("AVAILABLE_PREF_MAYBE_IMPACT").setValue(-5L);
    EmployeeConstraintMultiplier employeeConstraintMultiplier = new EmployeeConstraintMultiplier()
        .setEmployeeId("1").setName("AVAILABILITY_MAYBE").setMultiplier(2.0d);

    ksession.insert(keyValueFact);
    ksession.insert(employeeConstraintMultiplier);
    ksession.insert(employee);
    ksession.insert(shift);
    ksession.insert(availability);

    ksession.fireAllRules(new RuleNameMatchesAgendaFilter(".*AVAILABILITY_MAYBE.*"));

    assertEquals(-10L, getScoreHolder().getSoftScore());
  }

}

