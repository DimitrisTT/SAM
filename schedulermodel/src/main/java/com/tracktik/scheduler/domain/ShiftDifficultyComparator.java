package com.tracktik.scheduler.domain;

import java.io.Serializable;
import java.util.Comparator;

public class ShiftDifficultyComparator implements Comparator<Shift>, Serializable {

  @Override
  public int compare(Shift shift1, Shift shift2) {
    return shift1.getStart().compareTo(shift2.getStart());
  }

}
