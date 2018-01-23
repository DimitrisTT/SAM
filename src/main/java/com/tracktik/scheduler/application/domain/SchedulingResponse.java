package com.tracktik.scheduler.application.domain;

import com.tracktik.scheduler.application.SolverStatus;
import com.tracktik.scheduler.domain.Schedule;
import com.tracktik.scheduler.domain.Shift;

import java.util.ArrayList;
import java.util.List;

public class SchedulingResponse {

  public String id;
  public SolverStatus status;
  public List<Shift> shifts = new ArrayList<>();
  public SchedulingResponseMetadata meta;

  public SchedulingResponse(Schedule schedule, Long hardScore, Long softScore, SolverStatus _status) {
    id = schedule.getId();
    status = _status;
    shifts = schedule.getShifts();
    meta = new SchedulingResponseMetadata(hardScore, softScore);
  }

}
