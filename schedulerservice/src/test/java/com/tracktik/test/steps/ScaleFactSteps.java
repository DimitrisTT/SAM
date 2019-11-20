package com.tracktik.test.steps;

import com.tracktik.scheduler.configuration.*;
import com.tracktik.scheduler.domain.*;
import cucumber.api.DataTable;
import cucumber.api.java8.En;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class ScaleFactSteps implements En {

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
    String scaleId;
  }

  class TestScale {
    String id;
    String scaleTag;
    String rating;
  }


  Set<EmployeeConstraintMultiplier> ecmSet;
  Set<EmployeeSiteDistance> esdSet;
  Set<Employee> employees;
  Site site;
  PayType payType;
  Set<Skill> softSkillSet;
  Set<Skill> hardSkillSet;
  Post post;
  Shift shift;
  Shift secondShift;
  Set<String> tags;
  Set<Shift> shifts;
  Set<EmployeeAvailability> eaSet;
  Set<TimeOff> timesOff;
  Set<Scale> scaleSet;

  ScaleFact scaleFact;
  Impact impact;

  public ScaleFactSteps(DroolsTestApi droolsTestApi) {
    Given("^A Scale Fact is set to type '(.*?)'$", (String type) -> {
      scaleFact = new ScaleFact();
      scaleFact.setScaleType(ScaleType.valueOf(type));
    });
    And("^its Tag is set to '(.*?)'$", (String tag) -> {
    scaleFact.setScaleTag(ScaleTag.valueOf(tag));
    });
    And("^its rating is set to '(.*?)'$", (String rating) -> {
      scaleFact.setRating(Integer.parseInt(rating));
    });
    And("^its Id is set to '(.*?)'$", (String id) -> {
      scaleFact.setId(Integer.parseInt(id));
    });
    And("^its post_id is set to '(.*?)'$", (String postId) -> {
      scaleFact.setPostId(Integer.parseInt(postId));
    });
    And("^the ScaleFact impact has square '(.*?)' and impact of '(.*?)'$", (String square, String impactImpact) -> {
      impact = new Impact();
      impact.setSquare(Boolean.parseBoolean(square));
      impact.setImpact(Integer.parseInt(impactImpact));
      scaleFact.setImpact(impact);
    });

    Given("^a site with id '(.*?)' name '(.*?)' longitude '(.*?)' and latitude '(.*?)'$", (String id, String name, String longitude, String latitude) -> {
      site = new Site();
      site.setId(id);
      site.setName(name);
      site.setLongitude(Double.parseDouble(longitude));
      site.setLatitude(Double.parseDouble(latitude));
    });

    And("^the following scales$", (DataTable table) -> {
      Set<Scale> scales = table.asList(TestScale.class).stream().map(testScale -> {
        return new Scale()
                .setId(testScale.id)
                .setScaleTag(ScaleTag.valueOf(testScale.scaleTag))
                .setRating(Integer.parseInt(testScale.rating));
      }).collect(Collectors.toSet());
      scaleSet = scales;
    });
    And("^the following scaled shift from '(.*?)' to '(.*?)' timestamp '(.*?)' and end '(.*?)' with duration '(.*?)' id '(.*?)' and plan '(.*?)' available$",
            (String startDateTime, String endDateTime, String startTimeStamp, String endTimeStamp, String duration, String shiftId, String plan) -> {
              shift = new Shift();
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
    And("^the following employees with a scale$", (DataTable table) -> {
      esdSet = new HashSet<EmployeeSiteDistance>();
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
                .setScales(scaleSet.stream().filter(scale -> scale.getId().equals(testEmployee.scaleId)).collect(Collectors.toList()));
      }).collect(Collectors.toSet());
      if(employees == null) employees = new HashSet<Employee>();
      employees = employeesList;
    });
    And("^we apply each scaled employee into the shift for the calculation$", () -> {
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
        shifts = new HashSet<Shift>();
        shifts.add(newShift);
        droolsTestApi.ksession.insert(newShift);
      }
    });
    And("^the following post with id '(.*?)' name '(.*?)' bill rate '(.*?)' and pay rate '(.*?)' and a scale fact$", (String id, String name, String billRate, String payRate) -> {
      Set<ScaleFact> scaleFacts = new HashSet<ScaleFact>();
      scaleFacts.add(scaleFact);
      post = new Post();
      post.setId(id);
      post.setName(name);
      post.setPayType(payType);
      post.setSoftSkills(softSkillSet);
      post.setHardSkills(hardSkillSet);
      post.setScaleFacts(scaleFacts);
      post.setSite(site);
      post.setBillRate(Long.parseLong(billRate));
      post.setPayRate(Long.parseLong(payRate));
    });
    When("^Scale Fact rules are calculated$", () -> {
      System.out.println("employees" + employees);
      System.out.println("shift " + shifts);
      System.out.println("esdSet" + esdSet);
      System.out.println("ecmSet" + ecmSet);
      System.out.println("ScaleFact" + scaleFact);
      droolsTestApi.ksession.insert(scaleFact);
      esdSet.forEach(droolsTestApi.ksession::insert);
      employees.forEach(droolsTestApi.ksession::insert);
      droolsTestApi.ksession.fireAllRules();
    });

    After(() -> {
      scaleFact = null;
      impact = null;
      ecmSet = null;
      esdSet = null;
      employees = null;
      site = null;
      payType = null;
      softSkillSet = null;
      hardSkillSet = null;
      post = null;
      shift = null;
      secondShift = null;
      tags = null;
      shifts = null;
      eaSet = null;
      timesOff= null;
    });
  }
}
