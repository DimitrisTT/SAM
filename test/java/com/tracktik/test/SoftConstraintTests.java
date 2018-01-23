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
public class SoftConstraintTests {

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
  public void testEmployeeHasTwoSoftSkills() {

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

    Post post = new Post().setSoftSkills(postSkills);

    Shift shift = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setPost(post);

    ksession.insert(shift);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter( "has some requested soft skills" ));

    assertEquals(100L, scoreHolder.getSoftScore());
  }

  @Test
  public void testEmployeeHasNoSoftSkills() {

    HardSoftLongScoreHolder scoreHolder = new HardSoftLongScoreHolder(true);
    ksession.setGlobal("scoreHolder", scoreHolder);

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

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter( "has some requested soft skills" ));

    assertEquals(0L, scoreHolder.getSoftScore());
  }

}
