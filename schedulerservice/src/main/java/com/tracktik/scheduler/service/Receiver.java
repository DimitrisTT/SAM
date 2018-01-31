package com.tracktik.scheduler.service;

import com.tracktik.scheduler.domain.Schedule;
import com.tracktik.scheduler.domain.SchedulingResponse;
import com.tracktik.scheduler.domain.SolverStatus;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

  private static final SolverFactory<Schedule> solverFactory = SolverFactory.createFromXmlResource("schedulerConfig.xml");
  Logger logger = LoggerFactory.getLogger(Receiver.class);
  @Autowired
  private JmsTemplate jmsTemplate;

  @JmsListener(destination = "schedule.request", containerFactory = "schedulerFactory")
  public void receiveMessage(Schedule schedule) {

    logger.info("Got request to schedule");
    jmsTemplate.convertAndSend("schedule.reponse", new SchedulingResponse(schedule, null, null, SolverStatus.SOLVING));

    Solver<Schedule> solver = solverFactory.buildSolver();

    solver.addEventListener(event -> {
      logger.info("Updating new solution " + event.getNewBestScore().toShortString());
      if (event.getNewBestSolution().getScore().isFeasible()) {
        HardSoftLongScore score = (HardSoftLongScore) event.getNewBestScore();
        jmsTemplate.convertAndSend("schedule.reponse", new SchedulingResponse(event.getNewBestSolution(), score.getHardScore(), score.getSoftScore(), SolverStatus.SOLVING));
      }
    });
    logger.info("Optimizing schedule");
    Schedule solvedSchedule = solver.solve(schedule);

    HardSoftLongScore score = (HardSoftLongScore) solver.getBestScore();
    jmsTemplate.convertAndSend("schedule.response", new SchedulingResponse(solvedSchedule, score.getHardScore(), score.getSoftScore(), SolverStatus.COMPLETED));

    logger.info("Schedule solved");

  }

}
