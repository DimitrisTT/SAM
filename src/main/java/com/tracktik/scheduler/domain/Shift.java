package com.tracktik.scheduler.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.GregorianCalendar;

@PlanningEntity(difficultyComparatorClass = ShiftDifficultyComparator.class, movableEntitySelectionFilter = LockedShiftSelectionFilter.class)
public class Shift {

  private static final Logger logger = LoggerFactory.getLogger(Shift.class);

  private String id;
  private Boolean plan = false;
  private TimeSlot timeSlot;
  private Post post;
  private Long startTimeStamp;
  private Long endTimeStamp;

  @PlanningVariable(valueRangeProviderRefs = "employees")
  private Employee employee;

  public Shift() {
  }

  public TimeSlot getTimeSlot() {
    return timeSlot;
  }

  public Shift setTimeSlot(TimeSlot timeSlot) {
    this.timeSlot = timeSlot;
    return this;
  }

  public LocalTime getStartTime() {
    return LocalDateTime.ofInstant(timeSlot.getStart().toInstant(), ZoneId.systemDefault()).toLocalTime();
  }

  public LocalTime getEndTime() {
    return LocalDateTime.ofInstant(timeSlot.getEnd().toInstant(), ZoneId.systemDefault()).toLocalTime();
  }

  public long getHours() {
    return Duration.between(timeSlot.getStart().toInstant(), timeSlot.getEnd().toInstant()).toHours();
  }

  public Boolean overlaps(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {

    LocalDateTime shiftStart = timeSlot.getStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().plus(1L, ChronoUnit.SECONDS);
    LocalDateTime shiftEnd = timeSlot.getEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().minus(1L, ChronoUnit.SECONDS);

    LocalDateTime availableStart = shiftStart.with(dayOfWeek).withHour(startTime.getHour()).withMinute(startTime.getMinute()).withSecond(startTime.getSecond());
    LocalDateTime availableEnd = shiftEnd.with(dayOfWeek).withHour(endTime.getHour()).withMinute(endTime.getMinute()).withSecond(endTime.getSecond());

    Boolean overlaps = shiftEnd.isAfter(availableStart) && shiftStart.isBefore(availableEnd);

    logger.debug("Overlaps: " + overlaps + " shiftStart " + shiftStart + " shiftEnd " + shiftEnd + " availableStart " + availableStart + " availableEnd " + availableEnd);

    return overlaps;

  }

  public Boolean overlaps(EmployeeAvailability availability) {
    return this.overlaps(availability.getDayOfWeek(), availability.getStartTime(), availability.getEndTime());
  }

  public Post getPost() {
    return post;
  }

  public Shift setPost(Post post) {
    this.post = post;
    return this;
  }

  public Employee getEmployee() {
    return employee;
  }

  public Shift setEmployee(Employee employee) {
    this.employee = employee;
    return this;
  }

  public Boolean getPlan() {
    return plan;
  }

  public Shift setPlan(Boolean plan) {
    this.plan = plan;
    return this;
  }

  public Long getStartTimeStamp() {
    return startTimeStamp;
  }

  public Shift setStartTimeStamp(Long startTimeStamp) {
    this.startTimeStamp = startTimeStamp;
    return this;
  }

  public Long getEndTimeStamp() {
    return endTimeStamp;
  }

  public Shift setEndTimeStamp(Long endTimeStamp) {
    this.endTimeStamp = endTimeStamp;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Shift shift = (Shift) o;

    return id.equals(shift.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  public String getId() {
    return id;
  }

  public Shift setId(String id) {
    this.id = id;
    return this;
  }

  @Override
  public String toString() {
    return "Shift{" +
        "id='" + id + '\'' +
        ", plan=" + plan +
        ", timeSlot=" + timeSlot +
        ", post=" + post +
        ", employee=" + employee +
        '}';
  }
}
