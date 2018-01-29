package com.tracktik.test;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
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
import java.text.SimpleDateFormat;

@RunWith(SpringJUnit4ClassRunner.class)
public class ConstraintRuleTestBase {

  public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  public static KieContainer kieContainer;
  KieSession ksession;

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
    HardSoftLongScoreHolder scoreHolder = new HardSoftLongScoreHolder(true);
    ksession.setGlobal("scoreHolder", scoreHolder);
  }

  @After
  public void tearDown() {
    if (ksession != null) {
      ksession.dispose();
    }
  }

  public HardSoftLongScoreHolder getScoreHolder() {
    return (HardSoftLongScoreHolder) ksession.getGlobal("scoreHolder");
  }

  public Boolean sessionContainsClass(Class clazz) {
    return ksession.getObjects(obj -> obj.getClass() == clazz).size() != 0;
  }

}
