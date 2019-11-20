package com.tracktik.test;

import com.tracktik.scheduler.domain.Schedule;
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
import org.optaplanner.test.impl.score.buildin.hardsoftlong.HardSoftLongScoreVerifier;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
public class ConstraintRuleTestBase {


  public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  public static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  public static KieContainer kieContainer;
  KieSession ksession;

  @BeforeClass
  public static void setupContainer() {

    KieServices kieServices = KieServices.Factory.get();
    KieModuleModel kieModuleModel = kieServices.newKieModuleModel();
    KieBaseModel kieBaseModel = kieModuleModel.newKieBaseModel("KBase").setDefault(true);
    KieSessionModel ksessionModel = kieBaseModel.newKieSessionModel("KSession").setDefault(true);
    KieFileSystem kFileSystem = kieServices.newKieFileSystem();

    Arrays.asList("src/main/resources/availabilityConstraintRules.drl",
        "src/main/resources/constraintMultiplierRules.drl",
        "src/main/resources/functions.drl",
        "src/main/resources/experienceConstraintRules.drl",
        //"src/main/resources/overtimeConstraintRules.drl",
        "src/main/resources/proximityConstraintRules.drl",
        "src/main/resources/scaleRules.drl",
        "src/main/resources/schedulerScoreRules.drl").forEach(name -> {
          File file = new File(name);
          Resource resource = kieServices.getResources().newFileSystemResource(file).setResourceType(ResourceType.DRL);
          kFileSystem.write(resource);
    });

    KieBuilder kbuilder = kieServices.newKieBuilder(kFileSystem);
    kbuilder.buildAll();

    System.out.println("Knowledge base built");
    kbuilder.getResults().getMessages().forEach(message -> {
      System.out.println("Builder result message: " + message);
    });

    if (kbuilder.getResults().hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
      throw new RuntimeException("Build time Errors: " + kbuilder.getResults().toString());
    }
    kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
  }

  @Before
  public void setupSession() {
    ksession = kieContainer.getKieBase().newKieSession();
    //HardSoftLongScoreVerifier<Schedule> verifier;
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
