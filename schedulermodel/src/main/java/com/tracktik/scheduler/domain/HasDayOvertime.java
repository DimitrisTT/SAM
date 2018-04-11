package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class HasDayOvertime {
  final private String employeeId;
}
