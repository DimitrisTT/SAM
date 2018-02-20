package com.tracktik.scheduler.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

@Accessors(chain = true)
@Data
@EqualsAndHashCode(of = "employeeId")
@NoArgsConstructor
@AllArgsConstructor
public class TimeOff {

  private String employeeId;
  private Date startTime;
  private Date endTime;

  @JsonIgnore
  public LocalTime getStartLocalTime() {
    return LocalDateTime.ofInstant(startTime.toInstant(), ZoneId.systemDefault()).toLocalTime();
  }

  @JsonIgnore
  public LocalTime getEndLocalTime() {
    return LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault()).toLocalTime();
  }

}
