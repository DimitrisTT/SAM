package com.tracktik.scheduler.domain;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;

import java.util.ArrayList;
import java.util.List;

@PlanningSolution
public class Schedule {

	private HardSoftLongScore score;

	@ValueRangeProvider(id = "employees")
	@ProblemFactCollectionProperty
	private List<Employee> employees = new ArrayList<Employee>();

	@PlanningEntityCollectionProperty
	private List<Shift> shifts = new ArrayList<Shift>();
	
	@ProblemFactCollectionProperty
	private List<Post> posts = new ArrayList<Post>();
	@ProblemFactCollectionProperty
	private List<Site> sites = new ArrayList<Site>();
	

	@PlanningScore
	public HardSoftLongScore getScore() {
		return score;
	}

	public void setScore(HardSoftLongScore score) {
		this.score = score;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public List<Shift> getShifts() {
		return shifts;
	}
	public Schedule addShift(Shift shift) {
		shifts.add(shift);
		return this;
	}

	public void setShifts(List<Shift> shifts) {
		this.shifts = shifts;
	}
	public Schedule addEmployee(Employee employee) {
		employees.add(employee);
		return this;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public List<Site> getSites() {
		return sites;
	}

	public void setSites(List<Site> sites) {
		this.sites = sites;
	}

}
