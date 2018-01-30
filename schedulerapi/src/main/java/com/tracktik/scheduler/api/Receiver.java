package com.tracktik.scheduler.api;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.tracktik.scheduler.api.domain.SchedulingResponse;
import com.tracktik.scheduler.domain.Schedule;
import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class Receiver {

  Logger logger = LoggerFactory.getLogger(Receiver.class);

  @JmsListener(destination = "tracktik.scheduler", containerFactory = "schedulerFactory")
  public void receiveMessage(Schedule schedule) {

    logger.info("Got request to schedule");

    SolverFactory<Schedule> solverFactory = SolverFactory.createFromXmlResource(
        "schedulerConfig.xml");
    Solver<Schedule> solver = solverFactory.buildSolver();

    solver.addEventListener(event -> {
      logger.info("Updating new solution " + event.getNewBestScore().toShortString());
      if (event.getNewBestSolution().getScore().isFeasible()) {
        HardSoftLongScore score = (HardSoftLongScore) event.getNewBestScore();
        Session.solutions.put(schedule.getId(), new SchedulingResponse(event.getNewBestSolution(), score.getHardScore(), score.getSoftScore(), SolverStatus.SOLVING));
      }
    });
    logger.info("Optimizing schedule");
    Session.solutions.put(schedule.getId(), new SchedulingResponse(schedule, null, null, SolverStatus.SOLVING));
    Schedule solvedSchedule = solver.solve(schedule);

    HardSoftLongScore score = (HardSoftLongScore) solver.getBestScore();
    Session.solutions.put(schedule.getId(), new SchedulingResponse(solvedSchedule, score.getHardScore(), score.getSoftScore(), SolverStatus.COMPLETED));

    logger.info("Schedule solved");

  }

  private Score initialScore() {
    return new Score() {
      @Override
      public int getInitScore() {
        return 0;
      }

      @Override
      public boolean isSolutionInitialized() {
        return false;
      }

      @Override
      public Score toInitializedScore() {
        return null;
      }

      @Override
      public Score withInitScore(int i) {
        return null;
      }

      @Override
      public Score add(Score score) {
        return null;
      }

      @Override
      public Score subtract(Score score) {
        return null;
      }

      @Override
      public Score multiply(double v) {
        return null;
      }

      @Override
      public Score divide(double v) {
        return null;
      }

      @Override
      public Score power(double v) {
        return null;
      }

      @Override
      public Score negate() {
        return null;
      }

      @Override
      public Number[] toLevelNumbers() {
        return new Number[0];
      }

      @Override
      public boolean isCompatibleArithmeticArgument(Score score) {
        return false;
      }

      @Override
      public String toShortString() {
        return null;
      }

      @Override
      public int compareTo(Object o) {
        return 0;
      }
    };
  }
}
