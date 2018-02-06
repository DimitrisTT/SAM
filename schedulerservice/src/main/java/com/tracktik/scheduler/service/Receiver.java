package com.tracktik.scheduler.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracktik.scheduler.api.domain.QueueNames;
import com.tracktik.scheduler.domain.*;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.score.constraint.ConstraintMatchTotal;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class Receiver {

  Logger logger = LoggerFactory.getLogger(Receiver.class);
  @Autowired
  private JmsTemplate jmsTemplate;

  @JmsListener(destination = QueueNames.request, containerFactory = "schedulerFactory")
  public void receiveMessage(Schedule schedule) {

    //solveSchedule(schedule);
    new Thread(() -> this.solveSchedule(schedule)).start();

  }

  private void solveSchedule(Schedule schedule) {

    long startTime = System.currentTimeMillis();
    int totalShifts = schedule.getShifts().size();
    long totalSiftsToSchedule = schedule.getShifts().stream().filter(Shift::getPlan).count();
    int totalEmployees = schedule.getEmployees().size();

    logger.info("Got request to schedule " + totalSiftsToSchedule + " shifts out of " + totalShifts + " for " + totalEmployees + " employees. id: " + schedule.getId());

    SolverFactory<Schedule> solverFactory = SolverFactory.createFromXmlResource("schedulerConfig.xml");
    SchedulingResponse response = new SchedulingResponse().setId(schedule.getId()).setStatus(SolverStatus.SOLVING);
    jmsTemplate.convertAndSend(QueueNames.response, response);

    Solver<Schedule> solver = solverFactory.buildSolver();
    ScoreDirector<Schedule> scoreDirector = solver.getScoreDirectorFactory().buildScoreDirector();

    solver.addEventListener(event -> {
      logger.info("Updating new solution " + schedule.getId() + " " + event.getNewBestScore().toShortString());
      //if (event.getNewBestSolution().getScore().isFeasible()) {

        scoreDirector.setWorkingSolution(event.getNewBestSolution());
        Set<ConstrainScore> scores = scoreDirector.getConstraintMatchTotals().stream().map(constraintMatchTotal -> {
          String constrainName = constraintMatchTotal.getConstraintName();
          HardSoftLongScore constrainScore = (HardSoftLongScore) constraintMatchTotal.getScoreTotal();
          return new ConstrainScore(constrainName, constrainScore.getSoftScore(), constrainScore.getHardScore());
        }).collect(Collectors.toSet());

        HardSoftLongScore score = (HardSoftLongScore) event.getNewBestScore();
        SchedulingResponse interimResponse = new SchedulingResponse()
            .setId(schedule.getId())
            .setStatus(SolverStatus.SOLVING)
            .setShifts(event.getNewBestSolution().getShifts());
        interimResponse.getMeta()
            .setConstraint_scores(scores)
            .setHard_constraint_score(score.getHardScore())
            .setSoft_constraint_score(score.getSoftScore());

        logger.info("Sending interim solution " + interimResponse.getId());
        jmsTemplate.convertAndSend(QueueNames.response, interimResponse);
      //}
    });
    logger.info("Optimizing schedule " + schedule.getId());
    Schedule solvedSchedule = solver.solve(schedule);

    scoreDirector.setWorkingSolution(solvedSchedule);
    Set<ConstrainScore> scores = scoreDirector.getConstraintMatchTotals().stream().map(constraintMatchTotal -> {
      String constrainName = constraintMatchTotal.getConstraintName();
      HardSoftLongScore constrainScore = (HardSoftLongScore) constraintMatchTotal.getScoreTotal();
      return new ConstrainScore(constrainName, constrainScore.getSoftScore(), constrainScore.getHardScore());
    }).collect(Collectors.toSet());

    HardSoftLongScore score = (HardSoftLongScore) solver.getBestScore();
    response.setShifts(solvedSchedule.getShifts()).setStatus(SolverStatus.COMPLETED);
    response.getMeta().setConstraint_scores(scores).setHard_constraint_score(score.getHardScore()).setSoft_constraint_score(score.getSoftScore());

    response.getMeta().setTime_to_solve(System.currentTimeMillis() - startTime);

    jmsTemplate.convertAndSend(QueueNames.response, response);

    logger.info("Schedule solved for " + response.getId());

  }

}
