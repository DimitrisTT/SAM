package com.tracktik.scheduler.service;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DailyHours {
  final private String employeeId;
  final private LocalDate date;
  final private Long hours;
}
