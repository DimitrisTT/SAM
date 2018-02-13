package com.tracktik.scheduler.api.domain;

import com.tracktik.scheduler.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class RequestForScheduling {

  Logger logger = LoggerFactory.getLogger(RequestForScheduling.class);

  public Set<RequestShift> shifts = new HashSet<>();
  public Set<RequestPost> posts = new HashSet<>();
  public Set<RequestSkill> skills = new HashSet<>();
  public Set<RequestPostSkill> post_skills = new HashSet<>();
  public Set<RequestSite> sites = new HashSet<>();
  public Set<RequestEmployee> employees = new HashSet<>();
  public Set<RequestEmployeeSkill> employee_skills = new HashSet<>();
  public Set<RequestEmployeePostAssignment> employees_to_posts = new HashSet<>();
  public Set<RequestEmployeeSiteAssignment> employees_to_sites = new HashSet<>();
  public Set<RequestTimeOff> time_off = new HashSet<>();
  public Set<RequestEmployeeAvailability> employee_availabilities = new HashSet<>();
  public Map<String, Object> facts = new HashMap<>();
  public Set<RequestSiteBan> site_bans = new HashSet<>();
  public Map<String, Map<String,String>> employee_multipliers;

  /*
  public Schedule toSchedule(String id) {

    Schedule schedule = new Schedule();
    schedule.setId(id);

    Set<Skill> skillSet = skills.stream().parallel().map(requestSkill -> new Skill(requestSkill.id, requestSkill.description)).collect(Collectors.toSet());

    schedule.setSites(
        sites.stream().map(old -> {
          logger.debug("mapping: " + old);
          Site site = new Site()
              .setId(old.id)
              .setName(old.name);
          if (old.geo_lat != null && old.geo_lon != null) {
            site.setLatitude(new Double(old.geo_lat)).setLongitude(new Double(old.geo_lon));
          }

          return site;
        }).collect(Collectors.toSet())
    );

    //Map<String, Skill> skillsMap = skills.stream().collect(Collectors.toMap(skill -> skill.id, skill -> new Skill(skill.id, skill.description)));

    schedule.setPosts(posts.stream().map(old -> {
          logger.debug("mapping post: " + old);
          Post post = new Post();
          if (old.bill_rate != null) {
            Float billRate = new Float(old.bill_rate);
            billRate = billRate * 100;
            post.setBillRate(billRate.longValue());
          }
          if (old.pay_rate != null) {
            Float payRate = new Float(old.pay_rate);
            payRate = payRate * 100;
            post.setPayRate(payRate.longValue());
          }
          post.setId(old.id)
              .setName(old.name);
          if (old.pay_type.equals(PayType.EMPLOYEE_RATE.name())) post.setPayType(PayType.EMPLOYEE_RATE);
          if (old.pay_type.equals(PayType.POST_RATE.name())) post.setPayType(PayType.POST_RATE);
          post.setSite(
              schedule.getSites().stream().parallel().filter(site -> site.getId().equals(old.site_id)).findAny().get()
          );

          Set<String> hardSkillIds = post_skills.stream().parallel().filter(skill -> skill.post_id.equals(old.id) && skill.type.equals(SkillType.HARD.name())).map(skill -> skill.skill_id).collect(Collectors.toSet());
          Set<String> softSkillIds = post_skills.stream().parallel().filter(skill -> skill.post_id.equals(old.id) && skill.type.equals(SkillType.SOFT.name())).map(skill -> skill.skill_id).collect(Collectors.toSet());

          post.setHardSkills(
              skillSet.stream().parallel().filter(skill -> hardSkillIds.contains(skill.getId())).collect(Collectors.toSet())
          );
          post.setSoftSkills(
              skillSet.stream().parallel().filter(skill -> softSkillIds.contains(skill.getId())).collect(Collectors.toSet())
          );
          return post;
        }).collect(Collectors.toSet())
    );

    employees.forEach(employee -> {
      schedule.addEmployee(
          new Employee()
              .setId(employee.id)
              .setAvailabilityPreference(null)  //TODO account
              .setCostFromFloatString(employee.pay_rate)
              .setName(employee.name)
              .setPreferredHours(0L)  //TODO account for this in the request payload
              .setLatitude(employee.geo_lat == null ? null : new Double(employee.geo_lat))
              .setLongitude(employee.geo_lon == null ? null : new Double(employee.geo_lon))
              .setSiteExperience(
                  employees_to_sites.stream().parallel()
                      .filter(employee_to_site -> employee_to_site.user_id.equals(employee.id))
                      .map(employee_to_site -> employee_to_site.site_id)
                      .map(site_id ->
                          schedule.getSites().stream().filter(site -> site.getId().equals(site_id)).findAny().get()
                      ).collect(Collectors.toList())
              )
              .setPostExperience(
                  employees_to_posts.stream().parallel()
                      .filter(employee_to_post -> employee_to_post.user_id.equals(employee.id))
                      .map(employee_to_post -> employee_to_post.post_id)
                      .map(post_id ->
                          schedule.getPosts().stream().filter(site -> site.getId().equals(post_id)).findAny().get()
                      ).collect(Collectors.toList())
              )
              .setSkills(
                  employee_skills.stream().parallel()
                      .filter(employee_skill -> employee_skill.employee_id.equals(employee.id))
                      .map(employee_skill -> employee_skill.skill_id)
                      .map(skill_id -> skillSet.stream().filter(skill -> skill.getId().equals(skill_id)).findAny().get())
                      .collect(Collectors.toList())
              )
      );
    });

    schedule.setShifts(
        shifts.stream().map(old -> {
          Shift shift = new Shift()
              .setId(old.shift_id)
              //.setStartDate(LocalDate.parse(old.start_date, DateTimeFormatter.ISO_LOCAL_DATE))
              .setPlan(old.plan == null || old.plan.equals("1"))
              //.setPlan(true)
              .setTimeSlot(new TimeSlot(old.start_date_time, old.end_date_time))
              .setStartTimeStamp(old.start_timestamp)
              .setEndTimeStamp(old.end_timestamp)
              .setDuration(new Float(old.duration))
              .setPost(
                  schedule.getPosts().stream().filter(post -> post.getId().equals(old.post_id)).findAny().get()
              );
            if (old.employee_id != null && !shift.getPlan()) {
              schedule.getEmployees().stream().filter(employee -> employee.getId().equals(old.employee_id)).findAny().ifPresent(shift::setEmployee);
            }
            return shift;
        }).collect(Collectors.toSet())
    );

    schedule.setTimesOff(time_off.stream().map(requestTimeOff -> {
      return new TimeOff(requestTimeOff.employee_id, new Date(new Long(requestTimeOff.start_time)), new Date(new Long(requestTimeOff.end_time)));
    }).collect(Collectors.toSet()));

    schedule.setEmployeeAvailabilities(
        employee_availabilities.stream().map(requestEmployeeAvailability -> {
          return new EmployeeAvailability()
              .setEmployeeId(requestEmployeeAvailability.employee_id)
              .setType(AvailabilityType.valueOf(requestEmployeeAvailability.type))
              .setDayOfWeek(DayOfWeek.of(new Integer(requestEmployeeAvailability.day_of_week)))
              .setStartTime(LocalTime.MIDNIGHT.plus(new Long(requestEmployeeAvailability.seconds_start), ChronoUnit.SECONDS))
              .setEndTime(LocalTime.MIDNIGHT.plus(new Long(requestEmployeeAvailability.seconds_end), ChronoUnit.SECONDS));
        }).collect(Collectors.toSet())
    );

    schedule.getEmployees().stream().filter(employee -> employee.getLatitude() != null && employee.getLongitude() != null).forEach(employee -> {
      schedule.getSites().stream().filter(site -> site.getLatitude() != null && site.getLongitude() != null).forEach(site -> {
        Long distance = distance(employee.getLatitude(), employee.getLongitude(), site.getLatitude(), site.getLongitude());
        schedule.getEmployeeSiteDistance().add(new EmployeeSiteDistance(employee.getId(), site.getId(), distance));
      });
    });

    //logger.info("distances: " + schedule.getEmployeeSiteDistance());
    return schedule;
  }
  private Long distance(Double geo1_latitude, Double geo1_longitude, Double geo2_latitude, Double geo2_longitude ) {
    Double distance = (Math.acos(Math.sin(geo1_latitude * Math.PI / 180D) * Math.sin(geo2_latitude * Math.PI / 180D) + Math.cos(geo1_latitude * Math.PI / 180D) * Math.cos(geo2_latitude * Math.PI / 180D) * Math.cos((geo1_longitude - geo2_longitude) * Math.PI / 180D)) * 180 / Math.PI) * 60 * 1.1515D;
    return distance.longValue();
  }
*/

  @Override
  public String toString() {
    return "RequestForScheduling{" +
        "shifts=" + shifts +
        ", posts=" + posts +
        ", skills=" + skills +
        ", post_skills=" + post_skills +
        ", sites=" + sites +
        ", employees=" + employees +
        ", employee_skills=" + employee_skills +
        ", employees_to_posts=" + employees_to_posts +
        ", employees_to_sites=" + employees_to_sites +
        ", time_off=" + time_off +
        ", employee_availabilities=" + employee_availabilities +
        ", facts=" + facts +
        ", site_bans=" + site_bans +
        ", employee_multipliers=" + employee_multipliers +
        '}';
  }
}
