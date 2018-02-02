package com.tracktik.scheduler.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracktik.scheduler.api.domain.RequestForScheduling;
import com.tracktik.scheduler.domain.*;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.score.constraint.ConstraintMatchTotal;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.persistence.common.api.domain.solution.SolutionFileIO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SchedulerSolutionFileIO implements SolutionFileIO<Schedule> {

  public String getInputFileExtension() {
    return "txt";
  }
  public Schedule read(File inputFile) {

    ObjectMapper mapper = new ObjectMapper();
    RequestForScheduling request = null;
    try {
      request = mapper.readValue(inputFile, RequestForScheduling.class);
    } catch (IOException e) {
      e.printStackTrace();
    }

    Schedule schedule = request.toSchedule(UUID.randomUUID().toString());
    int totalShifts = schedule.getShifts().size();
    long totalSiftsToSchedule = schedule.getShifts().stream().filter(Shift::getPlan).count();
    long totalLockedUnasigned = schedule.getShifts().stream().filter(shift -> !shift.getPlan()).filter(shift -> shift.getEmployee() == null).count();

    int totalEmployees = schedule.getEmployees().size();

    System.out.println("Got request to schedule " + totalSiftsToSchedule + " shifts out of " + totalShifts + " for " + totalEmployees + " employees. id: " + schedule.getId());
    System.out.println("Number of locked that are still unassigned " + totalLockedUnasigned);
    return schedule;
    /*
    try {
      String jsonString = FileUtils.readFileToString(inputFile, "UTF-8");
      JSONObject json = new JSONObject(jsonString);

      return marshall(json);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
    */

  }

  private Schedule marshall(JSONObject json) {

    Map<String, Employee> employees = createEmployees(json.getJSONArray("employees"));
    Map<String, Site> sites = createSites(json.getJSONArray("sites"));
    Map<String, Post> posts = createPosts(json.getJSONArray("posts"), sites);
    Map<String, Skill> skills = createSkills(json.getJSONArray("skills"));
    mapSkillsToPosts(json.getJSONArray("post_skills"), posts, skills);
    mapSkillsToEmployees(json.getJSONArray("employee_skills"), employees, skills);
    mapSitesToEmployees(json.getJSONArray("employees_to_sites"), sites, employees);
    Set<Shift> shifts = createShifts(json.getJSONArray("shifts"), posts);

    Schedule schedule = new Schedule();
    schedule.setEmployees(new HashSet<>(employees.values()));
    schedule.setPosts(new HashSet<Post>(posts.values()));
    schedule.setSites(new HashSet<Site>(sites.values()));
    schedule.setShifts(shifts);
    return schedule;

  }

  private void mapSitesToEmployees(JSONArray employeeToSites, Map<String, Site> sites, Map<String, Employee> employees) {
    for (Object employeeToSiteObject : employeeToSites) {
      JSONObject employeeToSiteJson = (JSONObject) employeeToSiteObject;
      Employee employee = employees.get(employeeToSiteJson.get("user_id"));
      Site site = sites.get(employeeToSiteJson.get("site_id"));
      if (employee != null && site != null) {
        employee.getSiteExperience().add(site);
      }
    }
  }

  private Set<Shift> createShifts(JSONArray shiftsJson, Map<String, Post> posts) {
    Set<Shift> shifts = new HashSet<>();
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    for (Object shiftObject : shiftsJson) {
      JSONObject shiftJson = (JSONObject) shiftObject;
      Shift shift = new Shift();
      shift.setPost(posts.get(shiftJson.get("post_id")));
      TimeSlot timeSlot = new TimeSlot();
      //LocalDateTime start = LocalDateTime.parse(shiftJson.getString("start_date_time"), format);
      //LocalDateTime end = LocalDateTime.parse(shiftJson.getString("end_date_time"), format);
      try {
        timeSlot.setStart(sdf.parse(shiftJson.getString("start_date_time")));
        timeSlot.setEnd(sdf.parse(shiftJson.getString("end_date_time")));
        shift.setTimeSlot(timeSlot);
      } catch (ParseException e) {
        e.printStackTrace();
      }
      shift.setId(shiftJson.getString("shift_id"));
      shifts.add(shift);
    }
    return shifts;
  }

  private void mapSkillsToEmployees(JSONArray employeeSkills, Map<String, Employee> employees,
                                    Map<String, Skill> skills) {
    for (Object skillObject : employeeSkills) {
      JSONObject skillJson = (JSONObject) skillObject;
      Employee employee = employees.get(skillJson.get("employee_id"));
      if (employee != null) {

        employee.getSkills().add(skills.get(skillJson.get("skill_id")));
      }

    }
  }

  private Map<String, Skill> createSkills(JSONArray skillsJson) {
    Map<String, Skill> skills = new HashMap<String, Skill>();
    for (Object skillObject : skillsJson) {
      JSONObject skillJson = (JSONObject) skillObject;
      Skill skill = new Skill();
      skill.setId(skillJson.getString("id"));
      skill.setDescription(skillJson.getString("description"));
      skills.put(skill.getId(), skill);
    }
    return skills;
  }

  private void mapSkillsToPosts(JSONArray postSkills, Map<String, Post> posts, Map<String, Skill> skills) {
    for (Object skillObject : postSkills) {
      JSONObject skillJson = (JSONObject) skillObject;
      Post post = posts.get(skillJson.get("post_id"));
      if (post != null) {

        if (skillJson.get("type").equals("HARD")) {
          post.getHardSkills().add(skills.get(skillJson.get("skill_id")));
        } else {
          post.getSoftSkills().add(skills.get(skillJson.get("skill_id")));
        }
      }
    }
  }

  private Map<String, Post> createPosts(JSONArray postsJson, Map<String, Site> sites) {
    Map<String, Post> posts = new HashMap<String, Post>();
    for (Object postObject : postsJson) {
      JSONObject postJson = (JSONObject) postObject;
      Post post = new Post();
      if (postJson.get("bill_rate").equals(JSONObject.NULL)) {
        post.setBillRate(20L);
      } else {
        post.setBillRate((Double.valueOf(postJson.getDouble("bill_rate") * 100)).longValue());
      }
      post.setId(postJson.getString("id"));
      post.setPayRate(post.getBillRate() - 5);
      post.setSoftSkills(new HashSet<Skill>());
      post.setHardSkills(new HashSet<Skill>());
      post.setSite(sites.get(postJson.get("site_id")));
      post.setName(postJson.getString("name"));
      posts.put(post.getId(), post);

    }

    return posts;

  }

  private Map<String, Site> createSites(JSONArray sitesJson) {
    Map<String, Site> sites = new HashMap<String, Site>();
    for (Object siteObject : sitesJson) {
      JSONObject siteJson = (JSONObject) siteObject;
      Site site = new Site();
      site.setId(siteJson.getString("id"));
      site.setName(siteJson.getString("name"));
      sites.put(site.getId(), site);
    }
    return sites;
  }

  private Map<String, Employee> createEmployees(JSONArray employeesJson) {
    Map<String, Employee> employees = new HashMap<String, Employee>();
    for (Object employeeObject : employeesJson) {
      JSONObject employeeJson = (JSONObject) employeeObject;
      Employee employee = new Employee();
      employee.setId(employeeJson.getString("id"));
      employee.setName(employeeJson.getString("name"));
      employees.put(employee.getId(), employee);
    }
    return employees;

  }

  public void write(Schedule solution, File file) {
    SolverFactory<Schedule> solverFactory = SolverFactory.createFromXmlResource(
        "schedulerConfig.xml");
    ScoreDirector<Schedule> scoreDirector = solverFactory.buildSolver().getScoreDirectorFactory().buildScoreDirector();
    scoreDirector.setWorkingSolution(solution);
    for (ConstraintMatchTotal cm : scoreDirector.getConstraintMatchTotals()) {
      System.out.println(cm.getConstraintName() + " : " + cm.getScoreTotal());
    }
    JSONObject results = new JSONObject();
    JSONArray shifts = new JSONArray();
    for (Shift shift : solution.getShifts()) {
      JSONObject shiftJson = new JSONObject();
      shiftJson.put("shift_id", shift.getId());
      shifts.put(shiftJson);
    }
    results.put("shifts", shifts);
    JSONObject meta = new JSONObject();
    meta.put("hard_constraint_score", ((HardSoftLongScore) scoreDirector.calculateScore()).getHardScore());
    meta.put("soft_constraint_score", ((HardSoftLongScore) scoreDirector.calculateScore()).getSoftScore());
    results.put("meta", meta);
    try (
        BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
      writer.write(results.toString());
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
