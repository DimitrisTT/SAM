package com.tracktik.scheduler.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.SECONDS;

@Accessors(chain = true)
@Data
public class TimeSlot {

  private static final Logger logger = LoggerFactory.getLogger(TimeSlot.class);

  private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  //private String id = UUID.randomUUID().toString();
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
    //Make sure the end is not inclusive
    end = new Date(end.toInstant().minus(1, SECONDS).toEpochMilli());
  }

  public Boolean overlaps(TimeSlot other, int hours) {

    Date thisEnd = new Date(this.getEnd().toInstant().plus(hours, HOURS).toEpochMilli());
    Date otherEnd = new Date(other.getEnd().toInstant().plus(hours, HOURS).toEpochMilli());

    return this.getStart().before(otherEnd) && other.getStart().before(thisEnd);
  }

  @JsonIgnore
  public Long getDurationHours() {

    //need to add a second to get hours since time slots are not full hours.
    Long hours = Duration.between(start.toInstant(), end.toInstant().plus(1, ChronoUnit.SECONDS)).toHours();
    return hours;
  }

  @JsonIgnore
  public Long getStartTime() {
    return start.getTime();
  }

  @JsonIgnore
  public Long getEndTime() {
    return end.getTime();
  }

}
