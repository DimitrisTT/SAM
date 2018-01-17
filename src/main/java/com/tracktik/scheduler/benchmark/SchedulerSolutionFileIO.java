package com.tracktik.scheduler.benchmark;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.optaplanner.persistence.common.api.domain.solution.SolutionFileIO;

import com.tracktik.scheduler.domain.Employee;
import com.tracktik.scheduler.domain.Post;
import com.tracktik.scheduler.domain.Schedule;
import com.tracktik.scheduler.domain.Shift;
import com.tracktik.scheduler.domain.Site;
import com.tracktik.scheduler.domain.Skill;
import com.tracktik.scheduler.domain.TimeSlot;

public class SchedulerSolutionFileIO implements SolutionFileIO<Schedule> {

	public String getInputFileExtension() {
		return "txt";
	}

	public Schedule read(File inputFile) {

		try {
			String jsonString = FileUtils.readFileToString(inputFile, "UTF-8");
			JSONObject json = new JSONObject(jsonString);

			return marshall(json);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	private Schedule marshall(JSONObject json) {

		Map<String, Employee> employees = createEmployees(json.getJSONArray("employees"));
		Map<String, Site> sites = createSites(json.getJSONArray("sites"));
		Map<String, Post> posts = createPosts(json.getJSONArray("posts"), sites);
		Map<String, Skill> skills = createSkills(json.getJSONArray("skills"));
		mapSkillsToPosts(json.getJSONArray("post_skills"), posts, skills);
		mapSkillsToEmployees(json.getJSONArray("employee_skills"), employees, skills);
		List<Shift> shifts = createShifts(json.getJSONArray("shifts"), posts);
		
		Schedule schedule = new Schedule();
		schedule.setEmployees(new ArrayList<Employee>(employees.values()));
		schedule.setPosts(new ArrayList<Post>(posts.values()));
		schedule.setSites(new ArrayList<Site>(sites.values()));
		schedule.setShifts(shifts);
		return schedule;

	}

	private List<Shift> createShifts(JSONArray shiftsJson, Map<String, Post> posts) {
		List<Shift> shifts = new ArrayList<Shift>();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		for(Object shiftObject: shiftsJson){
			JSONObject shiftJson = (JSONObject) shiftObject;
			Shift shift = new Shift();
			shift.setPost(posts.get(shiftJson.get("post_id")));
			TimeSlot timeSlot = new TimeSlot();
			LocalDateTime start = LocalDateTime.parse(shiftJson.getString("start_date_time"), format);
			LocalDateTime end = LocalDateTime.parse(shiftJson.getString("end_date_time"), format);
			timeSlot.setStart(start);
			timeSlot.setEnd(end);
			shift.setTimeSlot(timeSlot);
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
			post.setBillRate(Double.valueOf((postJson.getDouble("bill_rate") * 100)).longValue());
			post.setId(postJson.getString("id"));
			post.setPayRate(Double.valueOf((postJson.getDouble("pay_rate") * 100)).longValue());
			post.setSoftSkills(new ArrayList<Skill>());
			post.setHardSkills(new ArrayList<Skill>());
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

	public void write(Schedule arg0, File arg1) {
		// TODO Auto-generated method stub

	}

}
