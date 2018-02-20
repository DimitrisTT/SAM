package com.tracktik.scheduler.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Accessors(chain = true)
@Data
public class EmployeeAvailability {

  private String employeeId;
  private AvailabilityType type;
  private DayOfWeek dayOfWeek;
  @JsonSerialize(using = LocalTimeSerializer.class)
  @JsonDeserialize(using = LocalTimeDeserializer.class)
  private LocalTime startTime;
  @JsonSerialize(using = LocalTimeSerializer.class)
  @JsonDeserialize(using = LocalTimeDeserializer.class)
  private LocalTime endTime;

  private Long startSeconds;
  private Long endSeconds;

}
