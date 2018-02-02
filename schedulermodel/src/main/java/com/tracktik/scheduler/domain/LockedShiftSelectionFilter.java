package com.tracktik.scheduler.domain;

import com.tracktik.scheduler.domain.Schedule;
import com.tracktik.scheduler.domain.Shift;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public class LockedShiftSelectionFilter implements SelectionFilter<Schedule, Shift> {

  @Override
  public boolean accept(ScoreDirector<Schedule> scoreDirector, Shift shift) {
    return shift.getPlan();
  }
}
