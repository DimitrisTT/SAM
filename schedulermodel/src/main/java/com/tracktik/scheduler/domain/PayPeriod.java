package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.threeten.extra.Interval;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Accessors(chain = true)
@Data
public class PayPeriod {
  private String id;
  private LocalDateTime start;
  private LocalDateTime end;

  public Interval getInterval() {
    return Interval.of(start.atZone(ZoneId.systemDefault()).toInstant(), end.atZone(ZoneId.systemDefault()).toInstant());
  }
}
