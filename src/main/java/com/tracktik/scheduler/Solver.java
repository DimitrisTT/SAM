package com.tracktik.scheduler;

import org.optaplanner.core.api.solver.SolverFactory;

import com.tracktik.scheduler.domain.*;

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
