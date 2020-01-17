package com.tracktik.scheduler.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.threeten.extra.Interval;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

@PlanningEntity(difficultyComparatorClass = ShiftDifficultyComparator.class, movableEntitySelectionFilter = LockedShiftSelectionFilter.class)
@Accessors(chain = true)
@Data
@EqualsAndHashCode(of = "id")
public class Shift {

  private static final Logger logger = LoggerFactory.getLogger(Shift.class);

  private String id;
  private Boolean plan = false;
  //private TimeSlot timeSlot;
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime start;
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime end;
  private Float duration;
  private Post post;
  private Long startTimeStamp;
  private Long endTimeStamp;
  private Set<String> tags = new HashSet<>();
/*
  public Shift(String id, Boolean plan, LocalDateTime start, LocalDateTime end, Float duration, Post post, Long startTimeStamp, Long endTimeStamp, Set<String> tags, Employee employee) {
    this.id = id;
    this.plan = plan;
    this.start = start;
    this.end = end;
    this.duration = duration;
    this.post = post;
    this.startTimeStamp = startTimeStamp;
    this.endTimeStamp = endTimeStamp;
    this.tags = tags;
    this.employee = employee;
  }
*/
  @PlanningVariable(valueRangeProviderRefs = "employees")
  private Employee employee;

/*  @JsonIgnore
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
  }*/

  @JsonIgnore
  public long getHours() {
    return Duration.between(start, end).toHours();
  }

  public Long durationHours() {

    //need to add a second to get hours since time slots are not full hours.
    Long milliseconds = Duration.between(start, end.plus(1, ChronoUnit.SECONDS)).toMillis();
    //Long milliseconds = Duration.between(start, end).toMillis() * 100;
    //logger.info("Duration hours for: " + this.start + " " + this.end  + " " + milliseconds / (1000 * 60 * 60));
    return milliseconds / 36_000;
  }

  public Long durationHoursDuring(LocalDate startDate, LocalTime time, LocalDate endDate) {

    Interval shiftInterval = Interval.of(start.atZone(ZoneId.systemDefault()).toInstant(), end.plus(1, ChronoUnit.SECONDS).atZone(ZoneId.systemDefault()).toInstant());

    LocalDateTime otherStart = LocalDateTime.of(startDate, time);
    LocalDateTime otherEnd = LocalDateTime.of(endDate, time);

    Interval otherInterval = Interval.of(otherStart.atZone(ZoneId.systemDefault()).toInstant(), otherEnd.atZone(ZoneId.systemDefault()).toInstant());

    logger.info("durationHoursDuring shift" + shiftInterval + " other " + otherInterval);
    if (!shiftInterval.overlaps(otherInterval)) return 0L;

    Interval intersection = shiftInterval.intersection(otherInterval);

    logger.info("durationHoursDuring: " + intersection.toDuration().toMillis() + " " + intersection.toDuration().toMillis() / 36_000);
    return intersection.toDuration().toMillis() / 36_000;
  }

  public Long durationHoursOnDate(LocalDate date) {

    LocalDateTime startDateTime = date.atStartOfDay();
    LocalDateTime endDateTime = date.atStartOfDay().plusDays(1);
    //LocalDateTime endDateTime = date.atStartOfDay().plusDays(1).minusSeconds(1);

    Interval dateInterval = Interval.of(startDateTime.atZone(ZoneId.systemDefault()).toInstant(), endDateTime.atZone(ZoneId.systemDefault()).toInstant());
    Interval shiftInterval = getInterval();

    if (!dateInterval.overlaps(shiftInterval)) {
      return 0L;
    }

    Long duration = dateInterval.intersection(shiftInterval).toDuration().toMillis() / 36_000;

    return duration;
  }

  public Long durationHoursDuringPayPeriod(PayPeriod period) {

    Interval shiftInterval = getInterval();
    Interval payPeriodInterval = period.getInterval();

    if (!shiftInterval.overlaps(payPeriodInterval)) return 0L;

    Long hours = shiftInterval.intersection(payPeriodInterval).toDuration().toMillis() / 36_000;

    return hours;
  }

  public Boolean overlaps(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {

    //LocalDateTime shiftStart = timeSlot.getStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    //LocalDateTime shiftEnd = timeSlot.getEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

    LocalDateTime availableStart = start.with(dayOfWeek).withHour(startTime.getHour()).withMinute(startTime.getMinute()).withSecond(startTime.getSecond());
    LocalDateTime availableEnd = end.with(dayOfWeek).withHour(endTime.getHour()).withMinute(endTime.getMinute()).withSecond(endTime.getSecond());

    Boolean overlaps = end.isAfter(availableStart) && start.isBefore(availableEnd);

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

  public Interval getInterval() {
    return Interval.of(start.atZone(ZoneId.systemDefault()).toInstant(), end.atZone(ZoneId.systemDefault()).toInstant());
  }

  public Boolean includesTime(LocalDate date, LocalTime time, LocalDate endDate) {

    LocalDateTime otherStart = LocalDateTime.of(date, time);
    LocalDateTime otherEnd = LocalDateTime.of(endDate, time);

    Interval otherInterval = Interval.of(otherStart.atZone(ZoneId.systemDefault()).toInstant(), otherEnd.atZone(ZoneId.systemDefault()).toInstant());

    return getInterval().isConnected(otherInterval);

  }

  public Long numberOfMatches(Collection first, Collection other) {
    return first.stream().filter(other::contains).count();
  }

  public Boolean includesTime(LocalDate date, LocalTime time) {

    LocalDateTime localDateTime = LocalDateTime.of(date, time);

    if (start.isEqual(localDateTime)) return true;
    if (end.isEqual(localDateTime)) return true;
    if (start.isAfter(localDateTime) && end.isBefore(localDateTime)) return true;

    return false;
  }

  public void setTimes(){
    startTimeStamp = 0L;
    //startTimeStamp += start.getDayOfYear()*86400;
    //startTimeStamp += start.getHour()*3600;
    //startTimeStamp += start.getMinute()*60;
    //startTimeStamp += start.getSecond();

    endTimeStamp = 0L;
    //endTimeStamp += end.getDayOfYear()*86400;
    //endTimeStamp += end.getHour()*3600;
    //endTimeStamp += end.getMinute()*60;
    //endTimeStamp += end.getSecond();

  }

  public String getId() {
    return id;
  }

  public Boolean getPlan() {
    return plan;
  }

  public LocalDateTime getStart() {
    return start;
  }

  public LocalDateTime getEnd() {
    return end;
  }

  public Float getDuration() {
    return duration;
  }

  public Post getPost() {
    return post;
  }

  public Long getStartTimeStamp() {
    return startTimeStamp;
  }

  public Long getEndTimeStamp() {
    return endTimeStamp;
  }

  public Set<String> getTags() {
    return tags;
  }

  public Employee getEmployee() {
    return employee;
  }
}
