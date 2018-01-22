package com.tracktik.scheduler.application.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RequestShift {

  public String shift_id;

  public String start_date_time;
  public String end_date_time;
  public String duration;
  public Long start_timestamp;
  public Long end_timestamp;
  public String post_id;
  public String start_date;

  @Override
  public String toString() {
    return "RequestShift{" +
        "shift_id='" + shift_id + '\'' +
        ", start_date_time=" + start_date_time +
        ", end_date_time=" + end_date_time +
        ", duration=" + duration +
        ", start_timestamp=" + start_timestamp +
        ", end_timestamp=" + end_timestamp +
        ", post_id='" + post_id + '\'' +
        ", start_date=" + start_date +
        '}';
  }
}
