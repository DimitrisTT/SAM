package com.tracktik.scheduler.domain;

import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.score.director.ScoreDirector;

/*
 * This class is an implementation of the SelectionFilter interface.
 */
public class LockedShiftSelectionFilter implements SelectionFilter<Schedule, Shift> {

  /*
   * This method relates whether or not the ScoreDirector accepts a given shift
   * used primarily at ruleTime, calculated by the solver
   * @param scoreDirector this is the object that fills in the schedule with a possible solution
   * @param shift this is a given shift that is to be accepted
   */
  @Override
  public boolean accept(ScoreDirector<Schedule> scoreDirector, Shift shift) {
    return shift.getPlan();
  }
}
