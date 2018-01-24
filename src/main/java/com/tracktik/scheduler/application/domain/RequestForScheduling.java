package com.tracktik.scheduler.application.domain;

import com.tracktik.scheduler.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
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
  public Set<RequestEmployeeSiteAssignment> employees_to_sites = new HashSet<>();

  public Schedule toSchedule(String id) {
    logger.info("request: " + this);
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

    schedule.setShifts(
        shifts.stream().map(old -> {
          Shift shift = new Shift()
              .setId(old.shift_id)
              .setPlan(old.plan.equals("1"))
              .setTimeSlot(new TimeSlot(old.start_date_time, old.end_date_time))
              .setPost(
                  schedule.getPosts().stream().filter(post -> post.getId().equals(old.post_id)).findAny().get()
              );
          return shift;
        }).collect(Collectors.toSet())
    );

    employees.stream().forEach(employee -> {
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
              .setSkills(
                  employee_skills.stream().parallel()
                      .filter(employee_skill -> employee_skill.employee_id.equals(employee.id))
                      .map(employee_skill -> employee_skill.skill_id)
                      .map(skill_id -> skillSet.stream().filter(skill -> skill.getId().equals(skill_id)).findAny().get())
                      .collect(Collectors.toList())
              )
      );
    });

    return schedule;
  }

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
        ", employees_to_sites=" + employees_to_sites +
        '}';
  }
}
