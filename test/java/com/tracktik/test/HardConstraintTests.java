package com.tracktik.test;

import com.tracktik.scheduler.domain.*;
import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScoreHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
public class HardConstraintTests {

  KieSession ksession;
  private static KieContainer kieContainer;

  @BeforeClass
  public static void setupContainer() {

    KieServices kieServices = KieServices.Factory.get();
    KieModuleModel kieModuleModel = kieServices.newKieModuleModel();
    KieBaseModel kieBaseModel = kieModuleModel.newKieBaseModel("KBase").setDefault(true);
    KieSessionModel ksessionModel = kieBaseModel.newKieSessionModel("KSession").setDefault(true);
    KieFileSystem kFileSystem = kieServices.newKieFileSystem();
    File file = new File("src/main/resources/schedulerScoreRules.drl");
    Resource resource = kieServices.getResources().newFileSystemResource(file).setResourceType(ResourceType.DRL);
    kFileSystem.write(resource);
    KieBuilder kbuilder = kieServices.newKieBuilder(kFileSystem);
    kbuilder.buildAll();

    if (kbuilder.getResults().hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
      throw new RuntimeException("Build time Errors: " + kbuilder.getResults().toString());
    }
    kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
  }

  @Before
  public void setupSession() {
    ksession = kieContainer.getKieBase().newKieSession();
  }

  @After
  public void tearDown() {
    if (ksession != null) {
      ksession.dispose();
    }
  }

  @Test
  public void testOverlappingShifts() {

    HardSoftLongScoreHolder scoreHolder = new HardSoftLongScoreHolder(true);
    ksession.setGlobal("scoreHolder", scoreHolder);

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

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter( "employee can not work two shifts at same time" ));

    assertTrue(scoreHolder.getHardScore() < 0);
  }

  @Test
  public void testNonOverlappingShifts() {

    HardSoftLongScoreHolder scoreHolder = new HardSoftLongScoreHolder(true);
    ksession.setGlobal("scoreHolder", scoreHolder);

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

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter( "employee can not work two shifts at same time" ));

    assertEquals(0L, scoreHolder.getHardScore());
  }

  @Test
  public void testEmployeeHasHardSkills() {

    HardSoftLongScoreHolder scoreHolder = new HardSoftLongScoreHolder(true);
    ksession.setGlobal("scoreHolder", scoreHolder);

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

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter( "employee must have hard skills" ));

    assertEquals(0L, scoreHolder.getHardScore());
  }

  @Test
  public void testEmployeeMissingHardSkills() {

    HardSoftLongScoreHolder scoreHolder = new HardSoftLongScoreHolder(true);
    ksession.setGlobal("scoreHolder", scoreHolder);

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

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter( "employee must have hard skills" ));

    assertTrue(scoreHolder.getHardScore() < 0);
  }

}
