package com.tracktik.scheduler.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.experimental.Accessors;
import org.threeten.extra.Interval;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Accessors(chain = true)
@Data
public class HolidayPeriod {
  private String postId;
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime start;
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime end;
  private Long startTimeStamp = 0L;
  private Long endTimeStamp = 0L;

  public void setTimeStamps() {
    if(start != null) {
      startTimeStamp += start.getSecond();
      startTimeStamp += (start.getMinute() * 60);
      startTimeStamp += (start.getHour() * 3600);
      startTimeStamp += (start.getDayOfYear() * 86400);
    }
    if(end != null) {
      endTimeStamp += end.getSecond();
      endTimeStamp += (end.getMinute() * 60);
      endTimeStamp += (end.getHour() * 3600);
      endTimeStamp += (end.getDayOfYear() * 86400);
    }
  }

  public Interval getInterval() {
    return Interval.of(start.atZone(ZoneId.systemDefault()).toInstant(), end.atZone(ZoneId.systemDefault()).toInstant());
  }
}
