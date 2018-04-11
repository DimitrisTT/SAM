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

  public Interval getInterval() {
    return Interval.of(start.atZone(ZoneId.systemDefault()).toInstant(), end.atZone(ZoneId.systemDefault()).toInstant());
  }
}
