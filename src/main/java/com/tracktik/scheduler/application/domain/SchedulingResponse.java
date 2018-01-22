package com.tracktik.scheduler.application.domain;

import com.tracktik.scheduler.domain.Schedule;
import com.tracktik.scheduler.domain.Shift;
import org.optaplanner.core.api.score.Score;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SchedulingResponse {

  public List<Shift> shifts = new ArrayList<>();
  public SchedulingResponseMetadata meta;

  public SchedulingResponse(Schedule schedule, Long hardScore, Long softScore) {
    shifts = schedule.getShifts();
    meta = new SchedulingResponseMetadata(hardScore, softScore);
  }

}
