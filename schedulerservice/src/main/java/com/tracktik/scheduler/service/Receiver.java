package com.tracktik.scheduler.service;

import com.google.common.collect.EvictingQueue;
import com.tracktik.scheduler.api.domain.QueueNames;
import com.tracktik.scheduler.domain.*;

import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScoreHolder;
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
import java.util.*;
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
      Set<ConstraintScore> scores = scoreDirector.getConstraintMatchTotals().stream().map(constraintMatchTotal -> {
        String constrainName = constraintMatchTotal.getConstraintName();
        HardSoftLongScore constrainScore = (HardSoftLongScore) constraintMatchTotal.getScoreTotal();
        return new ConstraintScore(constrainName, constrainScore.getSoftScore(), constrainScore.getHardScore());
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
    Set<ConstraintScore> scores = scoreDirector.getConstraintMatchTotals().stream().map(constraintMatchTotal -> {
      String constrainName = constraintMatchTotal.getConstraintName();
      HardSoftLongScore constrainScore = (HardSoftLongScore) constraintMatchTotal.getScoreTotal();
      return new ConstraintScore(constrainName, constrainScore.getSoftScore(), constrainScore.getHardScore());
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
                    ConstraintScore constraintScore = new ConstraintScore();
                    constraintScore.setConstraint_name(constraintMatch.getConstraintName());
                    constraintScore.setHard_score(constraintMatchScore.getHardScore());
                    constraintScore.setSoft_score(constraintMatchScore.getSoftScore());
                    return constraintScore;
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

    // Shift Based Report:
    for( Shift shift: response.getShifts() ) {
      logger.info("Shift {}", shift.getId());
      logger.info("    Start: {}", shift.getStart());
      logger.info("    Duration: {}", shift.getDuration());
      logger.info("    Site Name: {}", shift.getPost().getSite().getName());
      logger.info("    Employee: {}", shift.getEmployee().getId());
      if (shift.getEmployee().getSiteExperience() != null) {
        logger.info("        site experience: yes");
      } else {
        logger.info("        site experience: no");
      }
      if (shift.getEmployee().getPostExperience() != null) {
        logger.info("        post experience: yes");
      } else {
        logger.info("        post experience: no");
      }
      if (shift.getEmployee().getSeniority() != null) {
        logger.info("        seniority: yes");
      } else {
        logger.info("        seniority: no");
      }
    }
    logger.info("Employee Totals: ");
    response.getShifts().stream().map(shift -> shift.getEmployee()).collect(Collectors.toSet())
      .stream().forEach(employee -> {
        Set<Shift> shifts = response.getShifts().stream().filter(shift -> shift.getEmployee() == employee).collect(Collectors.toSet());
        Duration totalDuration = shifts.stream().map(shift -> Duration.between(shift.getStart(), shift.getEnd())).reduce(Duration.ZERO, Duration::plus);
        logger.info("\tEmployee id: {}", employee.getId());
        logger.info("\tTotal duration assigned: {}", totalDuration);
    });

    // Employee Based Report:
    List<Employee> employees = new ArrayList<Employee>();
    List<HashSet<Shift>> shiftList = new ArrayList<HashSet<Shift>>();
    HashSet<Shift> shifts = new HashSet<Shift>();
    for( Shift shift: response.getShifts()){
      if(!employees.contains(shift.getEmployee())){
        employees.add(shift.getEmployee());
        shifts = new HashSet<Shift>();
        shifts.add(shift);
        shiftList.add(shifts);
      } else {
        shifts = shiftList.get(employees.indexOf(shift.getEmployee()));
        shifts.add(shift);
      }
    }

    for(Employee employee: employees){
      logger.info("Employee: {}", employee.getId());
      logger.info("    Shifts: [ {");
      for( Shift shift: shiftList.get(employees.indexOf(employee))) {
        logger.info("        Site name: {}", shift.getPost().getSite().getName());
        logger.info("        Start: {}", shift.getStart());
        logger.info("        Duration: {}", shift.getDuration());
        logger.info("    },{");
      }
//      System.out.println(employee.getClockwise());
        if(employee.getClockwise()!=null) {
          logger.info(employee.getClockwise().stringOvertimeSummary());
        }
      logger.info("    } ]");
    }

    logger.info("===========================================");
    logger.info("SolverStatus: {}", response.getStatus());
    logger.info("===========================================");
    //logger.info("Response object {}", response);

  }

}
