package com.tracktik.scheduler.application.domain;

import com.tracktik.scheduler.domain.*;

import java.util.*;
import java.util.stream.Collectors;

public class RequestForScheduling {

  public Set<RequestShift> shifts = new HashSet<>();
  public Set<RequestPost> posts = new HashSet<>();
  public Set<RequestSkill> skills = new HashSet<>();
  public Set<RequestPostSkill> post_skills = new HashSet<>();
  public Set<RequestSite> sites = new HashSet<>();
  public Set<RequestEmployee> employees = new HashSet<>();
  public Set<RequestEmployeeSkill> employee_skills = new HashSet<>();
  public Set<RequestEmployeeSiteAssignment> employees_to_sites = new HashSet<>();

  public Schedule toSchedule() {
    Schedule schedule = new Schedule();

    Set<Skill> skillSet = skills.stream().parallel().map(requestSkill -> new Skill(requestSkill.id, requestSkill.description)).collect(Collectors.toSet());

    schedule.setSites(
      sites.stream().map( old -> {
        return new Site()
            .setId(old.id).setName(old.name)
            .setLatitude(new Double(old.geo_lat)).setLongitude(new Double(old.geo_lon));
      }).collect(Collectors.toList())
    );

    //Map<String, Skill> skillsMap = skills.stream().collect(Collectors.toMap(skill -> skill.id, skill -> new Skill(skill.id, skill.description)));

    schedule.setPosts(posts.stream().map(old -> {
      Post post = new Post()
          .setBillRate(new Long(old.bill_rate))
          .setId(old.id)
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
      }).collect(Collectors.toList())
    );

    schedule.setShifts(
        shifts.stream().map(old -> {
          Shift shift = new Shift()
              .setId(old.shift_id)
              .setTimeSlot(new TimeSlot(old.start_date_time, old.end_date_time))
              .setPost(
                  schedule.getPosts().stream().filter(post -> post.getId().equals(old.post_id)).findAny().get()
              );
          return shift;
        }).collect(Collectors.toList())
    );

    employees.stream().forEach(employee -> {
      schedule.addEmployee(
          new Employee()
              .setId(employee.id)
              .setAvailabilityPreference(null)  //TODO account
              .setCost(new Long(employee.pay_rate))
              .setName(employee.name)
              .setPreferredHours(0L)  //TODO account for this in the request payload
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
