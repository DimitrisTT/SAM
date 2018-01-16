package com.tracktik.scheduler;

import com.tracktik.scheduler.domain.Employee;
import com.tracktik.scheduler.domain.Schedule;
import com.tracktik.scheduler.domain.Shift;
import org.optaplanner.core.api.solver.SolverFactory;

public class Solver {

  public static void main(String[] args) {

    SolverFactory<Schedule> solverFactory = SolverFactory.createFromXmlResource(
        "schedulerConfig.xml");
    org.optaplanner.core.api.solver.Solver<Schedule> solver = solverFactory.buildSolver();
    Schedule unsolvedSchedule = new Schedule();
    unsolvedSchedule
        .addEmployee(new Employee())
        .addShift(new Shift());
    Schedule solvedSchedule = solver.solve(unsolvedSchedule);

  }

}
