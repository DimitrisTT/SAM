package com.tracktik.scheduler.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static java.time.temporal.ChronoUnit.HOURS;

public class TimeSlot {

  private static final Logger logger = LoggerFactory.getLogger(TimeSlot.class);

  private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private Date start;
  private Date end;

  public TimeSlot() {
  }

  public TimeSlot(String sStartDateTime, String sEndDateTime) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try {
      start = sdf.parse(sStartDateTime);
      end = sdf.parse(sEndDateTime);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    /*
    start = LocalDateTime.parse(sStartDateTime, format);
    end = LocalDateTime.parse(sEndDateTime, format);
    */
  }

  public TimeSlot(Long startTime, Long endTime) {
    start = new Date(startTime);
    end = new Date(endTime);
    /*
    start = LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), TimeZone.getDefault().toZoneId());
    end = LocalDateTime.ofInstant(Instant.ofEpochMilli(endTime), TimeZone.getDefault().toZoneId());
    */
  }

  public Date getStart() {
    return start;
  }

  public TimeSlot setStart(Date start) {
    this.start = start;
    return this;
  }

  public Date getEnd() {
    return end;
  }

  public TimeSlot setEnd(Date end) {
    this.end = end;
    return this;
  }

  public Boolean overlaps(TimeSlot other, int hours) {

    if (other == null) logger.error("OTHER TIMESLOT WAS NULL");
    return false;
    /*
    logger.debug("Overlaps: " + other.toString(), " hours: " + hours);
    Date thisEnd = new Date(this.getEnd().toInstant().plus(hours, HOURS).toEpochMilli());
    Date otherEnd = new Date(other.getEnd().toInstant().plus(hours, HOURS).toEpochMilli());
    Boolean doesOverlap = this.getStart().before(otherEnd) && other.getStart().before(thisEnd);
    logger.debug("Overlapping: " + doesOverlap);
    return doesOverlap;
    */
  }

  public Long getDurationHours() {
    return Duration.between(start.toInstant(), end.toInstant()).toHours();
  }

  public Long getStartTime() { return start.getTime(); }
  public Long getEndTime() { return end.getTime(); }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((end == null) ? 0 : end.hashCode());
    result = prime * result + ((start == null) ? 0 : start.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TimeSlot other = (TimeSlot) obj;
    if (end == null) {
      if (other.end != null)
        return false;
    } else if (!end.equals(other.end))
      return false;
    if (start == null) {
      if (other.start != null)
        return false;
    } else if (!start.equals(other.start))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "TimeSlot{" +
        "start=" + start +
        ", end=" + end +
        '}';
  }
}
