package com.tracktik.scheduler.domain;

import java.time.LocalTime;

public class ClockInterval {

  final LocalTime start;
  final LocalTime end;

  private ClockInterval(LocalTime start, LocalTime end) {
    this.start = start;
    this.end = end;
  }

  static ClockInterval between(LocalTime start, LocalTime end) {
    return new ClockInterval(start, end);
  }

  Boolean contains(LocalTime time) {
    return !time.isBefore(start) && time.isBefore(end);
  }

  Boolean overlaps(ClockInterval interval) {
    return (start.isBefore(interval.end) || start.equals(interval.end)) && (interval.start.isBefore(end));
  }

}
