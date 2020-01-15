package com.tracktik.scheduler.service;

import com.google.common.collect.EvictingQueue;
import com.tracktik.scheduler.api.domain.QueueNames;
import com.tracktik.scheduler.domain.*;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class Receiver {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final SolverFactory<Schedule> standardSolverFactory = SolverFactory.createFromXmlResource("standardSchedulerConfig.xml");
  private final SolverFactory<Schedule> exhaustiveSolverFactory = SolverFactory.createFromXmlResource("exhaustiveSchedulerConfig.xml");

  @Autowired
  private JmsTemplate jmsTemplate;

  @JmsListener(destination = QueueNames.request, containerFactory = "schedulerFactory")
  public void receiveMessage(Schedule schedule) {

    //solveSchedule(schedule);
    new Thread(() -> this.solveSchedule(schedule)).start();

  }

  private void solveSchedule(Schedule schedule) {

    long startTime = System.currentTimeMillis();
    Instant startInstant = Instant.now();
    int totalShifts = schedule.getShifts().size();
    long totalShiftsToSchedule = schedule.getShifts().stream().filter(Shift::getPlan).count();
    int totalEmployees = schedule.getEmployees().size();

    logger.info("Got request to schedule {} shifts out of {} for {} employees. Schedule ID: {}", totalShiftsToSchedule, totalShifts, totalEmployees, schedule.getId());

    EvictingQueue<SchedulingResponse> bestSolutions = EvictingQueue.create(11);

    SchedulingResponse response = new SchedulingResponse().setId(schedule.getId()).setStatus(SolverStatus.SOLVING);
    jmsTemplate.convertAndSend(QueueNames.response, response);

    Solver<Schedule> solver;
    //if (totalShiftsToSchedule > 2) {
    //  //solver = standardSolverFactory.buildSolver();
      solver = standardSolverFactory.buildSolver();
    //} else {
    //  logger.info("Using exhaustive search");
    //  solver = exhaustiveSolverFactory.buildSolver();
    //}

    ScoreDirector<Schedule> scoreDirector = solver.getScoreDirectorFactory().buildScoreDirector();

    solver.addEventListener(event -> {

      if (!event.getNewBestScore().isSolutionInitialized()) return;

      logger.info("Updating new solution {} with score {}", schedule.getId(), event.getNewBestScore().toShortString());

      Long shiftsUnfilled = event.getNewBestSolution().getShifts().stream().filter(Shift::getPlan).filter(shift -> shift.getEmployee() == null).count();

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
          .setSoft_constraint_score(score.getSoftScore())
          .setSolution_is_feasible(score.isFeasible())
          .setNumber_of_shifts_unfilled(shiftsUnfilled);

      logger.info("Sending interim solution for {}", interimResponse.getId());
      jmsTemplate.convertAndSend(QueueNames.response, interimResponse);

      bestSolutions.add(interimResponse);
    });

    logger.info("Optimizing schedule {}", schedule.getId());
    Schedule solvedSchedule = solver.solve(schedule);

    Long shiftsUnfilled = solvedSchedule.getShifts().stream().filter(Shift::getPlan).filter(shift -> shift.getEmployee() == null).count();

    scoreDirector.setWorkingSolution(solvedSchedule);
    Set<ConstrainScore> scores = scoreDirector.getConstraintMatchTotals().stream().map(constraintMatchTotal -> {
      String constrainName = constraintMatchTotal.getConstraintName();
      HardSoftLongScore constrainScore = (HardSoftLongScore) constraintMatchTotal.getScoreTotal();
      return new ConstrainScore(constrainName, constrainScore.getSoftScore(), constrainScore.getHardScore());
    }).collect(Collectors.toSet());

    HardSoftLongScore score = (HardSoftLongScore) solver.getBestScore();
    response.setShifts(solvedSchedule.getShifts()).setStatus(SolverStatus.COMPLETED);
    response.getMeta()
        .setConstraint_scores(scores)
        .setHard_constraint_score(score.getHardScore())
        .setSoft_constraint_score(score.getSoftScore())
        .setSolution_is_feasible(score.isFeasible())
        .setNumber_of_shifts_unfilled(shiftsUnfilled);

    Set<ShiftAssignmentConstraintScores> shiftAssignmentScores = scoreDirector.getIndictmentMap().entrySet().stream()
        .filter(objectIndictmentEntry -> objectIndictmentEntry.getKey().getClass() == Shift.class)
        .map(objectIndictmentEntry -> {
          Shift shift = (Shift) objectIndictmentEntry.getKey();
          String shiftId = shift.getId();
          String employeeId = shift.getEmployee().getId();
          return new ShiftAssignmentConstraintScores()
              .setShift_id(shiftId)
              .setEmployee_id(employeeId)
              .setScores(
                  objectIndictmentEntry.getValue().getConstraintMatchSet().stream().map(constraintMatch -> {
                    HardSoftLongScore constraintMatchScore = (HardSoftLongScore) constraintMatch.getScore();
                    ConstrainScore constrainScore = new ConstrainScore();
                    constrainScore.setConstraint_name(constraintMatch.getConstraintName());
                    constrainScore.setHard_score(constraintMatchScore.getHardScore());
                    constrainScore.setSoft_score(constraintMatchScore.getSoftScore());
                    return constrainScore;
                  }).collect(Collectors.toSet())
              );
        }).collect(Collectors.toSet());
    response.getMeta().setShift_assignment_scores(shiftAssignmentScores);
    response.getMeta().setTime_to_solve(System.currentTimeMillis() - startTime);

    logger.info("Duration to complete {}", Duration.between(startInstant, Instant.now()).toString());
    if (bestSolutions.size() > 0) bestSolutions.remove(); //Remove the top since it is also the best solution over all.

    //Reverse the order so the best solutions are first
    bestSolutions.stream().map(schedulingResponse -> schedulingResponse.setStatus(SolverStatus.COMPLETED))
        .collect(Collectors.toCollection(ArrayDeque::new))
        .descendingIterator()
        .forEachRemaining(solution -> response.getNext_best_solutions().add(solution));

    jmsTemplate.convertAndSend(QueueNames.response, response);

    scoreDirector.close();
    logger.info("Schedule solved for {}", response.getId());
    int dvah = 0;
    int wamh = 0;
    int oa = 0;
    int sc = 0;
    int other = 0;
    for( Shift shift: response.getShifts() ) {
      logger.info("Shift {}", shift);
      switch (shift.getPost().getSite().getId()) {
        case "9721":
          oa++;
          break;
        case "9880":
          dvah++;
          break;
        case "9884":
          sc++;
          break;
        case "58080":
          wamh++;
          break;
        default:
          other++;
          break;
      }
    }
      logger.info("===================================");
      logger.info("SolverStatus: {}", response.getStatus());
      logger.info("===================================");
      logger.info("Total shifts per site: ");
      logger.info("Office Aurora: {}", Integer.toString(oa));
      logger.info("Denver VA Hospital: {}", Integer.toString(dvah));
      logger.info("Spot Check: {}", Integer.toString(sc));
      logger.info("Windsor at Meadow Hills: {}", Integer.toString(wamh));
      logger.info("Other sites: {}", Integer.toString(other));
    //logger.info("Response object {}", response);

  }

}
