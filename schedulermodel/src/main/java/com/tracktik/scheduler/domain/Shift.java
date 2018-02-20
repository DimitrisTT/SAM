package com.tracktik.scheduler.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.util.HashSet;
import java.util.Set;

@PlanningEntity(difficultyComparatorClass = ShiftDifficultyComparator.class, movableEntitySelectionFilter = LockedShiftSelectionFilter.class)
@Accessors(chain = true)
@Data
@EqualsAndHashCode(of = "id")
public class Shift {

  private static final Logger logger = LoggerFactory.getLogger(Shift.class);

  private String id;
  private Boolean plan = false;
  private TimeSlot timeSlot;
  private Float duration;
  private Post post;
  private Long startTimeStamp;
  private Long endTimeStamp;
  private Set<String> tags = new HashSet<>();

  @PlanningVariable(valueRangeProviderRefs = "employees")
  private Employee employee;

  @JsonIgnore
  public LocalTime getStartTime() {
    return LocalDateTime.ofInstant(timeSlot.getStart().toInstant(), ZoneId.systemDefault()).toLocalTime();
  }

  @JsonIgnore
  public LocalTime getEndTime() {
    return LocalDateTime.ofInstant(timeSlot.getEnd().toInstant(), ZoneId.systemDefault()).toLocalTime();
  }

  @JsonIgnore
  public LocalDate getStartDate() {
    return timeSlot.getStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
  }

  @JsonIgnore
  public long getHours() {
    return Duration.between(timeSlot.getStart().toInstant(), timeSlot.getEnd().toInstant()).toHours();
  }

  public Boolean overlaps(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {

    LocalDateTime shiftStart = timeSlot.getStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    LocalDateTime shiftEnd = timeSlot.getEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

    LocalDateTime availableStart = shiftStart.with(dayOfWeek).withHour(startTime.getHour()).withMinute(startTime.getMinute()).withSecond(startTime.getSecond());
    LocalDateTime availableEnd = shiftEnd.with(dayOfWeek).withHour(endTime.getHour()).withMinute(endTime.getMinute()).withSecond(endTime.getSecond());

    Boolean overlaps = shiftEnd.isAfter(availableStart) && shiftStart.isBefore(availableEnd);

    //logger.info("Overlaps: " + overlaps + " shiftStart " + shiftStart + " shiftEnd " + shiftEnd + " availableStart " + availableStart + " availableEnd " + availableEnd);

    return overlaps;

  }

  public Boolean overlaps(EmployeeAvailability availability) {
    //logger.info("checking overlap for shift: " + this.getId() + " employee id: " + this.getEmployee().getId() + " time slot: " + this.getTimeSlot().toString() + " availability " + availability);
    try {
      return this.overlaps(availability.getDayOfWeek(), availability.getStartTime(), availability.getEndTime());
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

}
