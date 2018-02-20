package com.tracktik.scheduler.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Accessors(chain = true)
@Data
@EqualsAndHashCode(of = "id")
public class SchedulingResponse {

  private String id;
  private SolverStatus status;
  private Set<Shift> shifts = new HashSet<>();
  private SchedulingResponseMetadata meta = new SchedulingResponseMetadata();
  private Set<SchedulingResponse> next_best_solutions = new HashSet<>();

}
