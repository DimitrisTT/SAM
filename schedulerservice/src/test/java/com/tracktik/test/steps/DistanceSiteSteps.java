package com.tracktik.test.steps;

import cucumber.api.DataTable;
import cucumber.api.java8.En;
import org.drools.core.base.RuleNameMatchesAgendaFilter;

import com.tracktik.scheduler.domain.Employee;
import com.tracktik.scheduler.domain.EmployeeSiteDistance;
import com.tracktik.scheduler.domain.EmployeeConstraintMultiplier;
import com.tracktik.scheduler.domain.Shift;
import com.tracktik.scheduler.domain.Post;
import com.tracktik.scheduler.domain.Site;
import com.tracktik.scheduler.domain.PayType;
import com.tracktik.scheduler.domain.Skill;
import com.tracktik.scheduler.configuration.FarFromSite;
import com.tracktik.scheduler.configuration.FairlyFarFromSite;
import com.tracktik.scheduler.configuration.CloseBySite;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import java.util.HashSet;
import java.util.ArrayList;

public class DistanceSiteSteps implements En {

  private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
/*
  class TestTimePeriod {
    String start;
    String end;
  }

  class TestPeriodOvertime {
    String min;
    String max;
    String type;
  }

  class TestDailyOvertime {
    String min;
    String max;
    String type;
  }

  class TestConsecutiveOvertime {
    String minDays;
    String maxDays;
    String minHours;
    String maxHours;
    String type;
  }
*/
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
/*
  class TestEmployeeSkill {
    String skillId;
    String employeeId;
  }
*/
  //Employee employee = new Employee().setId("1").setOvertimeRuleId("1").setPayScheduleId("1");
  //PayrollSchedule payrollSchedule = new PayrollSchedule().setId("1");
  //ZoneId zoneId;

  Set<EmployeeConstraintMultiplier> ecmSet = new HashSet<EmployeeConstraintMultiplier>();
  Set<EmployeeSiteDistance> esdSet = new HashSet<EmployeeSiteDistance>();
  FarFromSite farFromSite = new FarFromSite();
  Set<Employee> employees = new HashSet<Employee>();
  Site site = new Site();
  PayType payType;
  Set<Skill> softSkillSet = new HashSet<Skill>();
  Set<Skill> hardSkillSet = new HashSet<Skill>();
  Post post = new Post();
  Shift shift = new Shift();
  Set<String> tags = new HashSet<String>();
  Set<Shift> shifts = new HashSet<Shift>();
  FairlyFarFromSite fairlyFarFromSite = new FairlyFarFromSite();
  CloseBySite closeBySite = new CloseBySite();

  public DistanceSiteSteps(DroolsTestApi droolsTestApi) {
    Given("^Far From Site being active is '(.*?)'", (String active) -> {
      farFromSite.setActive(Boolean.parseBoolean(active));
    });
    And("^its definition is set to '(.*?)'", (String definition) -> {
      farFromSite.setDefinition(Integer.parseInt(definition));
    });
    And("^its score impact is '(.*?)'", (String scoreImpact) -> {
      farFromSite.setScoreImpact(Integer.parseInt(scoreImpact));
    });
    And("^it has is Hard Impact set to '(.*?)'", (String isHardImpact) -> {
      farFromSite.setHardImpact(Boolean.parseBoolean(isHardImpact));
    });

    Given("^Close By Site being active is '(.*?)'", (String active) -> {
      closeBySite.setActive(Boolean.parseBoolean(active));
    });
    And("^its Close By Site definition is set to '(.*?)'", (String definition) -> {
      closeBySite.setDefinition(Integer.parseInt(definition));
    });
    And("^its Close By Site score impact is '(.*?)'", (String scoreImpact) -> {
      closeBySite.setScoreImpact(Integer.parseInt(scoreImpact));
    });
    And("^it Close By Site has is Hard Impact set to '(.*?)'", (String isHardImpact) -> {
      closeBySite.setHardImpact(Boolean.parseBoolean(isHardImpact));
    });

    Given("^Fairly Far From Site being active is '(.*?)'", (String active) -> {
      fairlyFarFromSite.setActive(Boolean.parseBoolean(active));
    });
    And("^its Fairly Far From Site definition is set to '(.*?)'", (String definition) -> {
      fairlyFarFromSite.setDefinition(Integer.parseInt(definition));
    });
    And("^its Fairly Far From Site score impact is '(.*?)'", (String scoreImpact) -> {
      fairlyFarFromSite.setScoreImpact(Integer.parseInt(scoreImpact));
    });
    And("^it Fairly Far From Site has is Hard Impact set to '(.*?)'", (String isHardImpact) -> {
      fairlyFarFromSite.setHardImpact(Boolean.parseBoolean(isHardImpact));
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
                .setSkills(hardSkillSet.stream().filter(skill -> skill.getId().equals(testEmployee.skillId)).collect(Collectors.toList()));
      }).collect(Collectors.toSet());
      employees = employeesList;
    });
    /*
    And("^the employees have the following skills$", (DataTable table) -> {
      employee.setSkills(table.asList(TestEmployeeSkill.class).stream().map(testEmployeeSkill -> {
        employees.stream().filter(employee -> employee.getId().equals(testEmployeeSkill.employeeId))
                .map(skill -> testEmployeeSkill.skillId)
                .map(skillId -> hardSkillSet.stream().filter(skill -> skill.getId().equals(skillId)).findAny().get())
      }).collect(Collectors.toList());
        request.employee_skills.stream().parallel()
                .filter(employee_skill -> employee_skill.employee_id.equals(employee.id))
                .map(employee_skill -> employee_skill.skill_id)
                .map(skill_id -> skillSet.stream().filter(skill -> skill.getId().equals(skill_id)).findAny().get())
                .collect(Collectors.toList())
      });
    });*/
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
    And("^the following employee constraint multipliers$", (DataTable table) -> {
      Set<EmployeeConstraintMultiplier> employeeConstraintMultiplierSet= table.asList(TestEmployeeConstraintMultiplier.class).stream().map(testEmployeeConstraintMultiplier -> {
        return new EmployeeConstraintMultiplier()
                .setEmployeeId(testEmployeeConstraintMultiplier.id)
                .setName(testEmployeeConstraintMultiplier.name)
                .setMultiplier(Double.parseDouble(testEmployeeConstraintMultiplier.multiplier));
      }).collect(Collectors.toSet());
      ecmSet.forEach(droolsTestApi.ksession::insert);
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


//    Then("^softscore is (-?\\d+)$", (Integer softScore) -> {
 //     assertEquals(softScore.longValue(), droolsTestApi.getScoreHolder().getSoftScore());
  //  });
  }
}
 /*
    Given("^Payroll start of '(.*?)' '(.*?)' in '(.*?)'$", (String sStartDate, String sStartTime, String sTimeZone) -> {
      zoneId = ZoneId.of(sTimeZone);
      ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.of(LocalDate.parse(sStartDate), LocalTime.parse(sStartTime)), zoneId);
      payrollSchedule.setPeriodStartTime(zonedDateTime.toLocalTime()).setPeriodStartDate(zonedDateTime.toLocalDate());
    });
    And("^pay cycle frequency of (.*?)$", (String payCycleFrequency) -> {
      payrollSchedule.setFrequency(payCycleFrequency);
    });
    And("^hours spanning daily periods will be cut between periods$", () -> {
      payrollSchedule.setOverlappingMethod(OverlappingMethodType.CUT);
    });
    And("^hours spanning daily periods$", () -> {
      payrollSchedule.setOverlappingMethod(OverlappingMethodType.SPAN);
    });
    And("^holiday periods of$", (DataTable table) -> {
      Set<HolidayPeriod> periods = table.asList(TestTimePeriod.class).stream().map(testTimePeriod -> {
        return new HolidayPeriod()
            .setPostId("1")
            .setStart(LocalDateTime.parse(testTimePeriod.start, dateTimeFormatter.withZone(zoneId)))
            .setEnd(LocalDateTime.parse(testTimePeriod.end, dateTimeFormatter.withZone(zoneId)));
      }).collect(Collectors.toSet());
      periods.forEach(droolsTestApi.ksession::insert);
    });
    And("^employee shifts of$", (DataTable table) -> {
      AtomicInteger shiftId = new AtomicInteger(1);
      Set<Shift> shifts = table.asList(TestTimePeriod.class).stream().map(testShift -> {
        return new Shift()
            .setId(Integer.toString(shiftId.getAndIncrement()))
            .setPost(new Post().setId("1"))
            .setEmployee(employee)
            .setStart(LocalDateTime.parse(testShift.start, dateTimeFormatter))
            .setEnd(LocalDateTime.parse(testShift.end, dateTimeFormatter));
      }).collect(Collectors.toSet());
      System.out.println("shifts: " + shifts);
      shifts.forEach(droolsTestApi.ksession::insert);
    });
    And("^period overtime definitions with id '(.*?)' of$", (String id, DataTable table) -> {
      Set<PeriodOvertimeDefinition> periodOvertimeDefinitions = table.asList(TestPeriodOvertime.class).stream().map(testDefinition -> {
        return new PeriodOvertimeDefinition()
            .setId(id)
            .setOvertimeType(testDefinition.type)
            .setMinimumHours(new Long(testDefinition.min))
            .setMaximumHours(testDefinition.max.equals("INF") ? Long.MAX_VALUE : new Long(testDefinition.max));
      }).collect(Collectors.toSet());
      periodOvertimeDefinitions.forEach(droolsTestApi.ksession::insert);
    });
    And("^daily overtime definitions with id '(.*?)' of$", (String id, DataTable table) -> {
      Set<DayOvertimeDefinition> dayOvertimeDefinitions = table.asList(FarFromSiteSteps.TestDailyOvertime.class).stream().map(testDefinition -> {
        return new DayOvertimeDefinition()
            .setId(id)
            .setOvertimeType(testDefinition.type)
            .setMinimumHours(new Long(testDefinition.min))
            .setMaximumHours(testDefinition.max.equals("INF") ? Long.MAX_VALUE : new Long(testDefinition.max));
      }).collect(Collectors.toSet());
      dayOvertimeDefinitions.forEach(droolsTestApi.ksession::insert);
    });
    And("^consecutive day overtime definitions with id '(.*?)' of$", (String id, DataTable table) -> {
      Set<ConsecutiveDaysOvertimeDefinition> daysOvertimeDefinitions = table.asList(FarFromSiteSteps.TestConsecutiveOvertime.class).stream().map(testDefinition -> {
        return new ConsecutiveDaysOvertimeDefinition()
            .setId(id)
            .setOvertimeType(testDefinition.type)
            .setMinimumDay(new Long(testDefinition.minDays))
            .setMaximumDay(testDefinition.maxDays.equals("INF") ? Long.MAX_VALUE : new Long(testDefinition.maxDays))
            .setMinimumHours(new Long(testDefinition.minHours))
            .setMaximumHours(testDefinition.maxHours.equals("INF") ? Long.MAX_VALUE : new Long(testDefinition.maxHours));
      }).collect(Collectors.toSet());
      daysOvertimeDefinitions.forEach(droolsTestApi.ksession::insert);
    });
    And("^count holiday hours towards period overtime$", () -> {
      payrollSchedule.setCountHolidayHoursTowardsPeriodOvertime(true);
    });
    And("^holiday hours use start time$", () -> {
      payrollSchedule.setAlignHolidaysWithPeriodStartTime(true);
    });

  }
}
*/