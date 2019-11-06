package com.tracktik.test.steps;

import cucumber.api.DataTable;
import cucumber.api.java8.En;
import org.drools.core.base.RuleNameMatchesAgendaFilter;
import org.drools.core.base.RuleNameEqualsAgendaFilter;

import java.time.DayOfWeek;
import com.tracktik.scheduler.domain.Employee;
import com.tracktik.scheduler.domain.EmployeeSiteDistance;
import com.tracktik.scheduler.domain.EmployeeAvailability;
import com.tracktik.scheduler.domain.AvailabilityType;
import com.tracktik.scheduler.domain.EmployeeConstraintMultiplier;
import com.tracktik.scheduler.domain.Shift;
import com.tracktik.scheduler.domain.Post;
import com.tracktik.scheduler.domain.Site;
import com.tracktik.scheduler.domain.PayType;
import com.tracktik.scheduler.domain.Skill;
import com.tracktik.scheduler.configuration.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import java.util.HashSet;
import java.util.ArrayList;

public class ConfigurationSteps implements En {

  private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  class TestEmployee {
    String id;
    String name;
    String preferred_hours;
    String geo_lat;
    String geo_long;
    String pay_rate;
    String seniority;
    String minimum_rest_period;
    String skillId;
    String secondSkillId;
  }

  class TestSkill {
    String id;
    String description;
  }

  class TestEmployeeConstraintMultiplier {
    String id;
    String name;
    String multiplier;
  }

  class TestTag{
    String tag;
  }

  class TestEmployeeAvailability{
    String id;
    String availability_type;
    String day_of_week;
    String seconds_start;
    String seconds_end;
  }

  Set<EmployeeConstraintMultiplier> ecmSet = new HashSet<EmployeeConstraintMultiplier>();
  Set<EmployeeSiteDistance> esdSet = new HashSet<EmployeeSiteDistance>();
  Set<Employee> employees = new HashSet<Employee>();
  Site site = new Site();
  PayType payType;
  Set<Skill> softSkillSet = new HashSet<Skill>();
  Set<Skill> hardSkillSet = new HashSet<Skill>();
  Post post = new Post();
  Shift shift = new Shift();
  Shift secondShift = new Shift();
  Set<String> tags = new HashSet<String>();
  Set<Shift> shifts = new HashSet<Shift>();
  Set<EmployeeAvailability> eaSet = new HashSet<EmployeeAvailability>();

  FarFromSite farFromSite = new FarFromSite();
  FairlyFarFromSite fairlyFarFromSite = new FairlyFarFromSite();
  CloseBySite closeBySite = new CloseBySite();
  HardSkillMissing hardSkillMissing = new HardSkillMissing();
  SoftSkillMissing softSkillMissing = new SoftSkillMissing();
  LessThanExpectedHours lessThanExpectedHours = new LessThanExpectedHours();
  MoreThanExpectedHours moreThanExpectedHours = new MoreThanExpectedHours();
  NotAvailable notAvailable = new NotAvailable();
  MaybeAvailable maybeAvailable = new MaybeAvailable();
  MinimumRestPeriod minimumRestPeriod = new MinimumRestPeriod();
  NoExperienceAtSite noExperienceAtSite = new NoExperienceAtSite();
  NoExperienceAtPost noExperienceAtPost = new NoExperienceAtPost();
  CanNotWorkSimultaneousShifts canNotWorkSimultaneousShifts = new CanNotWorkSimultaneousShifts();

  public ConfigurationSteps(DroolsTestApi droolsTestApi) {
    Given("^Far From Site being active is '(.*?)'$", (String active) -> {
      farFromSite.setActive(Boolean.parseBoolean(active));
    });
    And("^its definition is set to '(.*?)'$", (String definition) -> {
      farFromSite.setDefinition(Integer.parseInt(definition));
    });
    And("^its score impact is '(.*?)'$", (String scoreImpact) -> {
      farFromSite.setScoreImpact(Integer.parseInt(scoreImpact));
    });
    And("^it has is Hard Impact set to '(.*?)'$", (String isHardImpact) -> {
      farFromSite.setHardImpact(Boolean.parseBoolean(isHardImpact));
    });

    Given("^Close By Site being active is '(.*?)'$", (String active) -> {
      closeBySite.setActive(Boolean.parseBoolean(active));
    });
    And("^its Close By Site definition is set to '(.*?)'$", (String definition) -> {
      closeBySite.setDefinition(Integer.parseInt(definition));
    });
    And("^its Close By Site score impact is '(.*?)'$", (String scoreImpact) -> {
      closeBySite.setScoreImpact(Integer.parseInt(scoreImpact));
    });
    And("^it Close By Site has is Hard Impact set to '(.*?)'$", (String isHardImpact) -> {
      closeBySite.setHardImpact(Boolean.parseBoolean(isHardImpact));
    });

    Given("^Fairly Far From Site being active is '(.*?)'$", (String active) -> {
      fairlyFarFromSite.setActive(Boolean.parseBoolean(active));
    });
    And("^its Fairly Far From Site definition is set to '(.*?)'$", (String definition) -> {
      fairlyFarFromSite.setDefinition(Integer.parseInt(definition));
    });
    And("^its Fairly Far From Site score impact is '(.*?)'$", (String scoreImpact) -> {
      fairlyFarFromSite.setScoreImpact(Integer.parseInt(scoreImpact));
    });
    And("^it Fairly Far From Site has is Hard Impact set to '(.*?)'$", (String isHardImpact) -> {
      fairlyFarFromSite.setHardImpact(Boolean.parseBoolean(isHardImpact));
    });

    Given("^Can Not Work Simultaneous Shifts being active is '(.*?)'$",  (String active) -> {
      canNotWorkSimultaneousShifts.setActive(Boolean.parseBoolean(active));
    });
    And("^its Can Not Work Simultaneous Shifts is Hard Failure is set to '(.*?)'$", (String isHardFailure) -> {
      canNotWorkSimultaneousShifts.setHardFailure(Boolean.parseBoolean(isHardFailure));
    });
    And("^its Can Not Work Simultaneous Shifts impact multiplier is '(.*?)'$", (String impactMultiplier) -> {
      canNotWorkSimultaneousShifts.setImpactMultiplier(Integer.parseInt(impactMultiplier));
    });
    And("^its Can Not Work Simultaneous Shifts is Hard Impact set to '(.*?)'", (String isHardImpact) -> {
      canNotWorkSimultaneousShifts.setHardImpact(Boolean.parseBoolean(isHardImpact));
    });

    Given("^Hard Skill Missing being active is '(.*?)'$",  (String active) -> {
      hardSkillMissing.setActive(Boolean.parseBoolean(active));
    });
    And("^its Hard Skill Missing is Hard Failure is set to '(.*?)'$", (String isHardFailure) -> {
      hardSkillMissing.setHardFailure(Boolean.parseBoolean(isHardFailure));
    });
    And("^its Hard Skill Missing impact multiplier is '(.*?)'$", (String impactMultiplier) -> {
      hardSkillMissing.setImpactMultiplier(Integer.parseInt(impactMultiplier));
    });
    And("^its Hard Skill Missing is Hard Impact set to '(.*?)'", (String isHardImpact) -> {
      hardSkillMissing.setHardImpact(Boolean.parseBoolean(isHardImpact));
    });

    Given("^Soft Skill Missing being active is '(.*?)'$",  (String active) -> {
      softSkillMissing.setActive(Boolean.parseBoolean(active));
    });
    And("^its Soft Skill Missing impact multiplier is '(.*?)'$", (String impactMultiplier) -> {
      softSkillMissing.setImpactMultiplier(Integer.parseInt(impactMultiplier));
    });
    And("^its Soft Skill Missing is Hard Impact set to '(.*?)'", (String isHardImpact) -> {
      softSkillMissing.setHardImpact(Boolean.parseBoolean(isHardImpact));
    });

    Given("^Less Than Expected Hours being active is '(.*?)'$", (String active) -> {
      lessThanExpectedHours.setActive(Boolean.parseBoolean(active));
    });
    And("^its Less Than Expected Hours impact is '(.*?)'$", (String impact) -> {
      lessThanExpectedHours.setImpact(Integer.parseInt(impact));
    });
    And("^it Less Than Expected Hours has is Hard Impact set to '(.*?)'", (String isHardImpact) -> {
      lessThanExpectedHours.setHardImpact(Boolean.parseBoolean(isHardImpact));
    });

    Given("^More Than Expected Hours being active is '(.*?)'$", (String active) -> {
      moreThanExpectedHours.setActive(Boolean.parseBoolean(active));
    });
    And("^its More Than Expected Hours impact is '(.*?)'$", (String impact) -> {
      moreThanExpectedHours.setImpact(Integer.parseInt(impact));
    });
    And("^it More Than Expected Hours has is Hard Impact set to '(.*?)'", (String isHardImpact) -> {
      moreThanExpectedHours.setHardImpact(Boolean.parseBoolean(isHardImpact));
    });

    Given("^Not Available being active is '(.*?)'$", (String active) -> {
      notAvailable.setActive(Boolean.parseBoolean(active));
    });
    And("^its Not Available score impact is '(.*?)'$", (String impact) -> {
      notAvailable.setImpact(Integer.parseInt(impact));
    });
    And("^it Not Available has is Hard Impact set to '(.*?)'", (String isHardImpact) -> {
      notAvailable.setHardImpact(Boolean.parseBoolean(isHardImpact));
    });

    Given("^Maybe Available being active is '(.*?)'$", (String active) -> {
      notAvailable.setActive(Boolean.parseBoolean(active));
    });
    And("^its Maybe Available score impact is '(.*?)'$", (String impact) -> {
      notAvailable.setImpact(Integer.parseInt(impact));
    });

    Given("^Minimum Rest Period being active is '(.*?)'$", (String active) -> {
      notAvailable.setActive(Boolean.parseBoolean(active));
    });
    And("^its Minimum Rest Period score impact is '(.*?)'$", (String impact) -> {
      notAvailable.setImpact(Integer.parseInt(impact));
    });
    And("^its Minimum Rest Period is hard impact is set to '(.*?)'", (String isHardImpact) -> {
      notAvailable.setHardImpact(Boolean.parseBoolean(isHardImpact));
    });

    Given("^No Experience At Site being active is '(.*?)'$", (String active) -> {
      noExperienceAtSite.setActive(Boolean.parseBoolean(active));
    });
    And("^its No Experience At Site score impact is '(.*?)'$", (String impact) -> {
      noExperienceAtSite.setImpact(Integer.parseInt(impact));
    });
    And("^it No Experience At Site has is Hard Impact set to '(.*?)'", (String isHardImpact) -> {
      noExperienceAtSite.setHardImpact(Boolean.parseBoolean(isHardImpact));
    });

    Given("^No Experience At Post being active is '(.*?)'$", (String active) -> {
      noExperienceAtPost.setActive(Boolean.parseBoolean(active));
    });
    And("^its No Experience At Post score impact is '(.*?)'$", (String impact) -> {
      noExperienceAtPost.setImpact(Integer.parseInt(impact));
    });
    And("^it No Experience At Post has is Hard Impact set to '(.*?)'", (String isHardImpact) -> {
      noExperienceAtPost.setHardImpact(Boolean.parseBoolean(isHardImpact));
    });


    Given("^the following site with id '(.*?)' name '(.*?)' longitude '(.*?)' and latitude '(.*?)'$", (String id, String name, String longitude, String latitude) -> {
      site.setId(id);
      site.setName(name);
      site.setLongitude(Double.parseDouble(longitude));
      site.setLatitude(Double.parseDouble(latitude));
    });
    And("^with payType '(.*?)'$", (String type) -> {
      payType.valueOf(type);
    });
    And("^the following soft skills$", (DataTable table) -> {
      Set<Skill> softSkills = table.asList(TestSkill.class).stream().map(testSkill -> {
        return new Skill()
                .setId(testSkill.id)
                .setDescription(testSkill.description);
      }).collect(Collectors.toSet());
      softSkillSet = softSkills;
    });
    And("^the following hard skills$", (DataTable table) -> {
      Set<Skill> hardSkills = table.asList(TestSkill.class).stream().map(testSkill -> {
        return new Skill()
                .setId(testSkill.id)
                .setDescription(testSkill.description);
      }).collect(Collectors.toSet());
      hardSkillSet = hardSkills;
    });
    And("^the following employees$", (DataTable table) -> {
      Set<Employee> employeesList = table.asList(TestEmployee.class).stream().map(testEmployee -> {
        esdSet.add(new EmployeeSiteDistance().setDistance(EmployeeSiteDistance.distance(Double.parseDouble(testEmployee.geo_long), Double.parseDouble(testEmployee.geo_lat), site.getLongitude(), site.getLatitude())).setEmployeeId(testEmployee.id).setSiteId(site.getId()));
        return new Employee()
                .setId(testEmployee.id)
                .setName(testEmployee.name)
                .setPreferredHours(Long.parseLong(testEmployee.preferred_hours))
                .setLatitude(Double.parseDouble(testEmployee.geo_lat))
                .setLongitude(Double.parseDouble(testEmployee.geo_long))
                .setCostFromFloatString(testEmployee.pay_rate)
                .setSeniority(Integer.parseInt(testEmployee.seniority))
                .setMinimumRestPeriod(Long.parseLong(testEmployee.minimum_rest_period))
                .setSkills(hardSkillSet.stream().filter(skill -> skill.getId().equals(testEmployee.skillId)||skill.getId().equals(testEmployee.secondSkillId)).collect(Collectors.toList()));
      }).collect(Collectors.toSet());
      employees = employeesList;
    });
    And("^the following employees with a soft skill$", (DataTable table) -> {
      Set<Employee> employeesList = table.asList(TestEmployee.class).stream().map(testEmployee -> {
        esdSet.add(new EmployeeSiteDistance().setDistance(EmployeeSiteDistance.distance(Double.parseDouble(testEmployee.geo_long), Double.parseDouble(testEmployee.geo_lat), site.getLongitude(), site.getLatitude())).setEmployeeId(testEmployee.id).setSiteId(site.getId()));
        return new Employee()
                .setId(testEmployee.id)
                .setName(testEmployee.name)
                .setPreferredHours(Long.parseLong(testEmployee.preferred_hours))
                .setLatitude(Double.parseDouble(testEmployee.geo_lat))
                .setLongitude(Double.parseDouble(testEmployee.geo_long))
                .setCostFromFloatString(testEmployee.pay_rate)
                .setSeniority(Integer.parseInt(testEmployee.seniority))
                .setMinimumRestPeriod(Long.parseLong(testEmployee.minimum_rest_period))
                .setSkills(softSkillSet.stream().filter(skill -> skill.getId().equals(testEmployee.skillId)||skill.getId().equals(testEmployee.secondSkillId)).collect(Collectors.toList()));
      }).collect(Collectors.toSet());
      employees = employeesList;
    });
    And("^the following employees with site experience$", (DataTable table) -> {
      Set<Employee> employeesList = table.asList(TestEmployee.class).stream().map(testEmployee -> {
        esdSet.add(new EmployeeSiteDistance().setDistance(EmployeeSiteDistance.distance(Double.parseDouble(testEmployee.geo_long), Double.parseDouble(testEmployee.geo_lat), site.getLongitude(), site.getLatitude())).setEmployeeId(testEmployee.id).setSiteId(site.getId()));
        return new Employee()
                .setId(testEmployee.id)
                .setName(testEmployee.name)
                .setPreferredHours(Long.parseLong(testEmployee.preferred_hours))
                .setLatitude(Double.parseDouble(testEmployee.geo_lat))
                .setLongitude(Double.parseDouble(testEmployee.geo_long))
                .setCostFromFloatString(testEmployee.pay_rate)
                .setSeniority(Integer.parseInt(testEmployee.seniority))
                .setMinimumRestPeriod(Long.parseLong(testEmployee.minimum_rest_period))
                .setSkills(hardSkillSet.stream().filter(skill -> skill.getId().equals(testEmployee.skillId)||skill.getId().equals(testEmployee.secondSkillId)).collect(Collectors.toList()));
      }).collect(Collectors.toSet());
      for(Employee employee: employeesList) {
        employee.addSiteExperience(site);
        employees.add(employee);
      }
    });
    And("^the following employees with post experience$", (DataTable table) -> {
      Set<Employee> employeesList = table.asList(TestEmployee.class).stream().map(testEmployee -> {
        esdSet.add(new EmployeeSiteDistance().setDistance(EmployeeSiteDistance.distance(Double.parseDouble(testEmployee.geo_long), Double.parseDouble(testEmployee.geo_lat), site.getLongitude(), site.getLatitude())).setEmployeeId(testEmployee.id).setSiteId(site.getId()));
        return new Employee()
                .setId(testEmployee.id)
                .setName(testEmployee.name)
                .setPreferredHours(Long.parseLong(testEmployee.preferred_hours))
                .setLatitude(Double.parseDouble(testEmployee.geo_lat))
                .setLongitude(Double.parseDouble(testEmployee.geo_long))
                .setCostFromFloatString(testEmployee.pay_rate)
                .setSeniority(Integer.parseInt(testEmployee.seniority))
                .setMinimumRestPeriod(Long.parseLong(testEmployee.minimum_rest_period))
                .setSkills(hardSkillSet.stream().filter(skill -> skill.getId().equals(testEmployee.skillId)||skill.getId().equals(testEmployee.secondSkillId)).collect(Collectors.toList()));
      }).collect(Collectors.toSet());
      for(Employee employee: employeesList) {
        employee.addPostExperience(post);
        employees.add(employee);
      }
    });
    And("^the following tags$", (DataTable table) -> {
      Set<String> tagsList = table.asList(TestTag.class).stream().map(testTag -> {
        return new String(testTag.tag);
      }).collect(Collectors.toSet());
      tags = tagsList;
    });
    And("^the following post with id '(.*?)' name '(.*?)' bill rate '(.*?)' and pay rate '(.*?)'$", (String id, String name, String billRate, String payRate) -> {
      post.setId(id);
      post.setName(name);
      post.setPayType(payType);
      post.setSoftSkills(softSkillSet);
      post.setHardSkills(hardSkillSet);
      post.setSite(site);
      post.setBillRate(Long.parseLong(billRate));
      post.setPayRate(Long.parseLong(payRate));
    });
    And("^the following shift from '(.*?)' to '(.*?)' timestamp '(.*?)' and end '(.*?)' with duration '(.*?)' id '(.*?)' and plan '(.*?)' available$",
            (String startDateTime, String endDateTime, String startTimeStamp, String endTimeStamp, String duration, String shiftId, String plan) -> {
              shift.setId(shiftId);
              shift.setPlan(Boolean.parseBoolean(plan));
              shift.setStart(LocalDateTime.parse(startDateTime, dateTimeFormatter));
              shift.setEnd(LocalDateTime.parse(endDateTime, dateTimeFormatter));
              shift.setDuration(Float.parseFloat(duration));
              shift.setPost(post);
              shift.setStartTimeStamp(Long.parseLong(startTimeStamp));
              shift.setEndTimeStamp(Long.parseLong(endTimeStamp));
              shift.setTags(tags);
            });
    And("^a second shift from '(.*?)' to '(.*?)' timestamp '(.*?)' and end '(.*?)' with duration '(.*?)' id '(.*?)' and plan '(.*?)' available$",
            (String startDateTime, String endDateTime, String startTimeStamp, String endTimeStamp, String duration, String shiftId, String plan) -> {
              secondShift.setId(shiftId);
              secondShift.setPlan(Boolean.parseBoolean(plan));
              secondShift.setStart(LocalDateTime.parse(startDateTime, dateTimeFormatter));
              secondShift.setEnd(LocalDateTime.parse(endDateTime, dateTimeFormatter));
              secondShift.setDuration(Float.parseFloat(duration));
              secondShift.setPost(post);
              secondShift.setStartTimeStamp(Long.parseLong(startTimeStamp));
              secondShift.setEndTimeStamp(Long.parseLong(endTimeStamp));
              secondShift.setTags(tags);
            });
    And("^the following employee constraint multipliers$", (DataTable table) -> {
      Set<EmployeeConstraintMultiplier> employeeConstraintMultiplierSet= table.asList(TestEmployeeConstraintMultiplier.class).stream().map(testEmployeeConstraintMultiplier -> {
        return new EmployeeConstraintMultiplier()
                .setEmployeeId(testEmployeeConstraintMultiplier.id)
                .setName(testEmployeeConstraintMultiplier.name)
                .setMultiplier(Double.parseDouble(testEmployeeConstraintMultiplier.multiplier));
      }).collect(Collectors.toSet());
      ecmSet = employeeConstraintMultiplierSet;
    });
    And("^we apply each employee into the shift for the calculation$", () -> {
      for(Employee employee : employees){
        Shift newShift = new Shift();
        newShift.setId(shift.getId());
        newShift.setPlan(shift.getPlan());
        newShift.setStart(shift.getStart());
        newShift.setEnd(shift.getEnd());
        newShift.setDuration(shift.getDuration());
        newShift.setPost(shift.getPost());
        newShift.setStartTimeStamp(shift.getStartTimeStamp());
        newShift.setEndTimeStamp(shift.getEndTimeStamp());
        newShift.setTags(shift.getTags());
        newShift.setEmployee(employee);
        shifts.add(newShift);
        droolsTestApi.ksession.insert(newShift);
      }
    });
    And("^we apply each employee into the second shift for the calculation$", () -> {
      for(Employee employee : employees){
        Shift newShift = new Shift();
        newShift.setId(secondShift.getId());
        newShift.setPlan(secondShift.getPlan());
        newShift.setStart(secondShift.getStart());
        newShift.setEnd(secondShift.getEnd());
        newShift.setDuration(secondShift.getDuration());
        newShift.setPost(secondShift.getPost());
        newShift.setStartTimeStamp(secondShift.getStartTimeStamp());
        newShift.setEndTimeStamp(secondShift.getEndTimeStamp());
        newShift.setTags(secondShift.getTags());
        newShift.setEmployee(employee);
        shifts.add(newShift);
        droolsTestApi.ksession.insert(newShift);
      }
    });
    And("^the following employee availabilities$", (DataTable table) -> {
      Set<EmployeeAvailability> employeeAvailabilities = table.asList(TestEmployeeAvailability.class).stream().map(testEmployeeAvailability -> {
        return new EmployeeAvailability()
                .setEmployeeId(testEmployeeAvailability.id)
                .setType(AvailabilityType.valueOf(testEmployeeAvailability.availability_type))
                .setDayOfWeek(DayOfWeek.of(Integer.parseInt(testEmployeeAvailability.day_of_week)))
                .setStartSeconds(Long.parseLong(testEmployeeAvailability.seconds_start))
                .setEndSeconds(Long.parseLong(testEmployeeAvailability.seconds_end))
                .setStartTime(LocalTime.MIDNIGHT.plusSeconds(Long.parseLong(testEmployeeAvailability.seconds_start)))
                .setEndTime(LocalTime.MIDNIGHT.plusSeconds(Long.parseLong(testEmployeeAvailability.seconds_end)));
      }).collect(Collectors.toSet());
      eaSet = employeeAvailabilities;
    });
    When("^Far From Site rules are calculated$", () -> {
  //    System.out.println("employees" + employees);
  //    System.out.println("shift " + shifts);
  //    System.out.println("esdSet" + esdSet);
  //    System.out.println("ecmSet" + ecmSet);
  //    System.out.println("FarFromSite" + farFromSite);
      droolsTestApi.ksession.insert(farFromSite);
      esdSet.forEach(droolsTestApi.ksession::insert);
      ecmSet.forEach(droolsTestApi.ksession::insert);
      employees.forEach(droolsTestApi.ksession::insert);
      droolsTestApi.ksession.fireAllRules();
    });

    When("^Fairly Far From Site rules are calculated$", () -> {
  //    System.out.println("employees" + employees);
  //    System.out.println("shift " + shifts);
  //    System.out.println("esdSet" + esdSet);
  //    System.out.println("ecmSet" + ecmSet);
  //    System.out.println("FairlyFarFromSite" + fairlyFarFromSite);
      droolsTestApi.ksession.insert(fairlyFarFromSite);
      esdSet.forEach(droolsTestApi.ksession::insert);
      ecmSet.forEach(droolsTestApi.ksession::insert);
      employees.forEach(droolsTestApi.ksession::insert);
      droolsTestApi.ksession.fireAllRules();
    });

    When("^Close By Site rules are calculated$", () -> {
  //    System.out.println("employees" + employees);
  //    System.out.println("shift " + shifts);
  //    System.out.println("esdSet" + esdSet);
  //    System.out.println("ecmSet" + ecmSet);
  //    System.out.println("CloseBySite" + closeBySite);
      droolsTestApi.ksession.insert(closeBySite);
      esdSet.forEach(droolsTestApi.ksession::insert);
      ecmSet.forEach(droolsTestApi.ksession::insert);
      employees.forEach(droolsTestApi.ksession::insert);
      droolsTestApi.ksession.fireAllRules();
    });

    When("^Hard Skill Missing rules are calculated$", () -> {
      //    System.out.println("employees" + employees);
      //    System.out.println("shift " + shifts);
      //    System.out.println("esdSet" + esdSet);
      //    System.out.println("ecmSet" + ecmSet);
      //    System.out.println("HardSkillMissing" + hardSkillMissing);
      droolsTestApi.ksession.insert(hardSkillMissing);
      esdSet.forEach(droolsTestApi.ksession::insert);
      ecmSet.forEach(droolsTestApi.ksession::insert);
      employees.forEach(droolsTestApi.ksession::insert);
      droolsTestApi.ksession.fireAllRules();
    });

    When("^Soft Skill Missing rules are calculated$", () -> {
      //    System.out.println("employees" + employees);
      //    System.out.println("shift " + shifts);
      //    System.out.println("esdSet" + esdSet);
      //    System.out.println("ecmSet" + ecmSet);
      //    System.out.println("SoftSkillMissing" + softSkillMissing);
      droolsTestApi.ksession.insert(softSkillMissing);
      esdSet.forEach(droolsTestApi.ksession::insert);
      ecmSet.forEach(droolsTestApi.ksession::insert);
      employees.forEach(droolsTestApi.ksession::insert);
      droolsTestApi.ksession.fireAllRules();
    });

    When("^Less Than Expected Hours rules are calculated$", () -> {
      //    System.out.println("employees" + employees);
      //    System.out.println("shift " + shifts);
      //    System.out.println("esdSet" + esdSet);
      //    System.out.println("ecmSet" + ecmSet);
      //    System.out.println("LessThanExpectedHours" + lessThanExpectedHours);
      droolsTestApi.ksession.insert(lessThanExpectedHours);
      esdSet.forEach(droolsTestApi.ksession::insert);
      ecmSet.forEach(droolsTestApi.ksession::insert);
      employees.forEach(droolsTestApi.ksession::insert);
      droolsTestApi.ksession.fireAllRules();
    });

    When("^More Than Expected Hours rules are calculated$", () -> {
      //    System.out.println("employees" + employees);
      //    System.out.println("shift " + shifts);
      //    System.out.println("esdSet" + esdSet);
      //    System.out.println("ecmSet" + ecmSet);
      //    System.out.println("CloseBySite" + closeBySite);
      droolsTestApi.ksession.insert(moreThanExpectedHours);
      esdSet.forEach(droolsTestApi.ksession::insert);
      ecmSet.forEach(droolsTestApi.ksession::insert);
      employees.forEach(droolsTestApi.ksession::insert);
      droolsTestApi.ksession.fireAllRules();
    });

    When("^Not Available rules are calculated$", () -> {
      //    System.out.println("employees" + employees);
      //    System.out.println("shift " + shifts);
      //    System.out.println("esdSet" + esdSet);
      //    System.out.println("ecmSet" + ecmSet);
      //    System.out.println("CloseBySite" + closeBySite);
      droolsTestApi.ksession.insert(notAvailable);
      esdSet.forEach(droolsTestApi.ksession::insert);
      ecmSet.forEach(droolsTestApi.ksession::insert);
      employees.forEach(droolsTestApi.ksession::insert);
      eaSet.forEach(droolsTestApi.ksession::insert);
      droolsTestApi.ksession.fireAllRules();
    });

    When("^Maybe Available rules are calculated$", () -> {
      //    System.out.println("employees" + employees);
      //    System.out.println("shift " + shifts);
      //    System.out.println("esdSet" + esdSet);
      //    System.out.println("ecmSet" + ecmSet);
      //    System.out.println("MaybeAvailable: " + maybeAvailable);
      droolsTestApi.ksession.insert(maybeAvailable);
      esdSet.forEach(droolsTestApi.ksession::insert);
      ecmSet.forEach(droolsTestApi.ksession::insert);
      employees.forEach(droolsTestApi.ksession::insert);
      eaSet.forEach(droolsTestApi.ksession::insert);
      droolsTestApi.ksession.fireAllRules();
    });

    When("^Minimum Rest Period rules are calculated$", () -> {
      //    System.out.println("employees" + employees);
      //    System.out.println("shift " + shifts);
      //    System.out.println("esdSet" + esdSet);
      //    System.out.println("ecmSet" + ecmSet);
      //    System.out.println("MinimumRestPeriod" + minimumRestPeriod);
      droolsTestApi.ksession.insert(minimumRestPeriod);
      esdSet.forEach(droolsTestApi.ksession::insert);
      ecmSet.forEach(droolsTestApi.ksession::insert);
      employees.forEach(droolsTestApi.ksession::insert);
      eaSet.forEach(droolsTestApi.ksession::insert);
      droolsTestApi.ksession.fireAllRules();
    });

    When("^No Experience At Site rules are calculated$", () -> {
      //  System.out.println("employees: " + employees);
      //  System.out.println("shifts: " + shifts);
      //  System.out.println("esdSet: " + esdSet);
      //  System.out.println("ecmSet: " + ecmSet);
      //  System.out.println("NoExperienceAtSite: " + noExperienceAtSite);
      droolsTestApi.ksession.insert(noExperienceAtSite);
      esdSet.forEach(droolsTestApi.ksession::insert);
      ecmSet.forEach(droolsTestApi.ksession::insert);
      employees.forEach(droolsTestApi.ksession::insert);
      eaSet.forEach(droolsTestApi.ksession::insert);
      droolsTestApi.ksession.fireAllRules();
    });

    When("^No Experience At Post rules are calculated$", () -> {
      //System.out.println("employees: " + employees);
      //System.out.println("shifts: " + shifts);
      //System.out.println("esdSet: " + esdSet);
      //System.out.println("ecmSet: " + ecmSet);
      //System.out.println("NoExperienceAtPost: " + noExperienceAtPost);
      droolsTestApi.ksession.insert(noExperienceAtPost);
      esdSet.forEach(droolsTestApi.ksession::insert);
      ecmSet.forEach(droolsTestApi.ksession::insert);
      employees.forEach(droolsTestApi.ksession::insert);
      eaSet.forEach(droolsTestApi.ksession::insert);
      droolsTestApi.ksession.fireAllRules();
    });

    When("^Can Not Work Simultaneous Shifts rules are calculated$", () -> {
      System.out.println("employees: " + employees);
      System.out.println("shifts: " + shifts);
      System.out.println("esdSet: " + esdSet);
      System.out.println("ecmSet: " + ecmSet);
      System.out.println("CanNotWorkSimultaneousShifts: " + canNotWorkSimultaneousShifts);
      droolsTestApi.ksession.insert(canNotWorkSimultaneousShifts);
      esdSet.forEach(droolsTestApi.ksession::insert);
      ecmSet.forEach(droolsTestApi.ksession::insert);
      employees.forEach(droolsTestApi.ksession::insert);
      eaSet.forEach(droolsTestApi.ksession::insert);
      droolsTestApi.ksession.fireAllRules();
    });

    Then("^hardscore is (-?\\d+)$", (Integer hardScore) -> {
      assertEquals(hardScore.longValue(), droolsTestApi.getScoreHolder().getHardScore());
    });
  }
}
