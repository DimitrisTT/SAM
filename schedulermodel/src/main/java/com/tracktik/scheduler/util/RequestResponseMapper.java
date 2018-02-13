package com.tracktik.scheduler.util;

import com.tracktik.scheduler.api.domain.RequestForScheduling;
import com.tracktik.scheduler.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.SECONDS;

public class RequestResponseMapper {

  private static final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private static final Logger logger = LoggerFactory.getLogger(RequestResponseMapper.class);

  private static Long distance(Double geo1_latitude, Double geo1_longitude, Double geo2_latitude, Double geo2_longitude ) {
    Double distance = (Math.acos(Math.sin(geo1_latitude * Math.PI / 180D) * Math.sin(geo2_latitude * Math.PI / 180D) + Math.cos(geo1_latitude * Math.PI / 180D) * Math.cos(geo2_latitude * Math.PI / 180D) * Math.cos((geo1_longitude - geo2_longitude) * Math.PI / 180D)) * 180 / Math.PI) * 60 * 1.1515D;
    return distance.longValue();
  }

  public static Schedule requestToSchedule(String id, RequestForScheduling request) {

    Schedule schedule = new Schedule();
    schedule.setId(id);

    Set<Skill> skillSet = request.skills.stream().parallel().map(requestSkill -> new Skill(requestSkill.id, requestSkill.description)).collect(Collectors.toSet());

    schedule.setSites(
        request.sites.stream().map(old -> {
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

    schedule.setPosts(request.posts.stream().map(old -> {
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

          Set<String> hardSkillIds = request.post_skills.stream().parallel().filter(skill -> skill.post_id.equals(old.id) && skill.type.equals(SkillType.HARD.name())).map(skill -> skill.skill_id).collect(Collectors.toSet());
          Set<String> softSkillIds = request.post_skills.stream().parallel().filter(skill -> skill.post_id.equals(old.id) && skill.type.equals(SkillType.SOFT.name())).map(skill -> skill.skill_id).collect(Collectors.toSet());

          post.setHardSkills(
              skillSet.stream().parallel().filter(skill -> hardSkillIds.contains(skill.getId())).collect(Collectors.toSet())
          );
          post.setSoftSkills(
              skillSet.stream().parallel().filter(skill -> softSkillIds.contains(skill.getId())).collect(Collectors.toSet())
          );
          return post;
        }).collect(Collectors.toSet())
    );

    request.employees.forEach(employee -> {
      schedule.addEmployee(
          new Employee()
              .setId(employee.id)
              .setAvailabilityPreference(null)  //TODO account
              .setCostFromFloatString(employee.pay_rate)
              .setName(employee.name)
              .setPreferredHours(employee.preferred_hours == null ? null : new Long(employee.preferred_hours))  //TODO account for this in the request payload
              .setLatitude(employee.geo_lat == null ? null : new Double(employee.geo_lat))
              .setLongitude(employee.geo_lon == null ? null : new Double(employee.geo_lon))
              .setSiteExperience(
                  request.employees_to_sites.stream().parallel()
                      .filter(employee_to_site -> employee_to_site.user_id.equals(employee.id))
                      .map(employee_to_site -> employee_to_site.site_id)
                      .map(site_id ->
                          schedule.getSites().stream().filter(site -> site.getId().equals(site_id)).findAny().get()
                      ).collect(Collectors.toList())
              )
              .setPostExperience(
                  request.employees_to_posts.stream().parallel()
                      .filter(employee_to_post -> employee_to_post.user_id.equals(employee.id))
                      .map(employee_to_post -> employee_to_post.post_id)
                      .map(post_id ->
                          schedule.getPosts().stream().filter(site -> site.getId().equals(post_id)).findAny().get()
                      ).collect(Collectors.toList())
              )
              .setSkills(
                  request.employee_skills.stream().parallel()
                      .filter(employee_skill -> employee_skill.employee_id.equals(employee.id))
                      .map(employee_skill -> employee_skill.skill_id)
                      .map(skill_id -> skillSet.stream().filter(skill -> skill.getId().equals(skill_id)).findAny().get())
                      .collect(Collectors.toList())
              )
              .setSeniority(employee.seniority == null ? null : new Integer(employee.seniority))
              .setMinimumRestPeriod(employee.minimum_rest_period == null ? new Long(8) : new Long(employee.minimum_rest_period))
      );
    });

    schedule.setShifts(
        request.shifts.stream().map(old -> {
          Date startDate = parseDate(old.start_date_time);
          Date endDate = parseDate(old.end_date_time);
          //Make sure the end is exclusive
          endDate = new Date(endDate.toInstant().minus(1, SECONDS).toEpochMilli());
          Shift shift = new Shift()
              .setId(old.shift_id)
              .setPlan(old.plan == null || old.plan.equals("1"))
              //The end of the timeslot will be exclusive
              .setTimeSlot(new TimeSlot().setStart(startDate).setEnd(endDate))
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

    schedule.setTimesOff(request.time_off.stream().map(requestTimeOff -> {
      return new TimeOff(requestTimeOff.employee_id, new Date(new Long(requestTimeOff.start_time)), new Date(new Long(requestTimeOff.end_time)));
    }).collect(Collectors.toSet()));

    schedule.setEmployeeAvailabilities(
        request.employee_availabilities.stream().map(requestEmployeeAvailability -> {
          return new EmployeeAvailability()
              .setEmployeeId(requestEmployeeAvailability.employee_id)
              .setType(AvailabilityType.valueOf(requestEmployeeAvailability.type))
              .setDayOfWeek(DayOfWeek.of(new Integer(requestEmployeeAvailability.day_of_week)))
              .setStartSeconds(new Long(requestEmployeeAvailability.seconds_start))
              .setEndSeconds(new Long(requestEmployeeAvailability.seconds_end))
              .setStartTime(LocalTime.MIDNIGHT.plus(new Long(requestEmployeeAvailability.seconds_start), ChronoUnit.SECONDS))
              //The end time will be exclusive
              .setEndTime(LocalTime.MIDNIGHT.plus(new Long(requestEmployeeAvailability.seconds_end) - 1, ChronoUnit.SECONDS));
        }).collect(Collectors.toSet())
    );

    schedule.getEmployees().stream().filter(employee -> employee.getLatitude() != null && employee.getLongitude() != null).forEach(employee -> {
      schedule.getSites().stream().filter(site -> site.getLatitude() != null && site.getLongitude() != null).forEach(site -> {
        Long distance = distance(employee.getLatitude(), employee.getLongitude(), site.getLatitude(), site.getLongitude());
        schedule.getEmployeeSiteDistance().add(new EmployeeSiteDistance(employee.getId(), site.getId(), distance));
      });
    });

    //logger.info("distances: " + schedule.getEmployeeSiteDistance());

    schedule.setKeyValueFacts(
        request.facts.entrySet().stream().map(entry -> {
          return new KeyValueFact().setKey(entry.getKey()).setValue(entry.getValue());
        }).collect(Collectors.toSet())
    );

    schedule.setSiteBans(
        request.site_bans.stream().map(ban -> new SiteBan().setEmployeeId(ban.employee_id).setSiteId(ban.site_id)).collect(Collectors.toSet())
    );

    schedule.setEmployeeConstraintMultipliers(
        request.employee_multipliers.entrySet().stream().flatMap(stringMapEntry -> {
          String employeeId = stringMapEntry.getKey();
          return stringMapEntry.getValue().entrySet().stream().map(stringStringEntry -> {
            return new EmployeeConstraintMultiplier()
                .setEmployeeId(employeeId)
                .setName(stringStringEntry.getKey())
                .setMultiplier(new Double(stringStringEntry.getValue()));
          });
        }).collect(Collectors.toSet())
    );

    return schedule;
  }

  private static Date parseDate(String sDateTime) {
    try {
      return dateTimeFormatter.parse(sDateTime);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }
}
