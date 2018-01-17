package com.tracktik.scheduler;

import org.optaplanner.benchmark.api.PlannerBenchmark;
import org.optaplanner.benchmark.api.PlannerBenchmarkFactory;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.persistence.common.api.domain.solution.SolutionFileIO;

import com.tracktik.scheduler.benchmark.SchedulerSolutionFileIO;
import com.tracktik.scheduler.domain.Employee;
import com.tracktik.scheduler.domain.Schedule;
import com.tracktik.scheduler.domain.Shift;

public class Solver {

	public static void main(String[] args) throws ClassNotFoundException {

		/*SolverFactory<Schedule> solverFactory = SolverFactory.createFromXmlResource(
				"schedulerConfig.xml");
		org.optaplanner.core.api.solver.Solver<Schedule> solver = solverFactory.buildSolver();
		Schedule unsolvedSchedule = new Schedule();
		unsolvedSchedule
			.addEmployee(new Employee())
			.addShift(new Shift());*/
		//Schedule solvedSchedule = solver.solve(unsolvedSchedule);
		
		PlannerBenchmarkFactory benchmarkFactory = PlannerBenchmarkFactory.createFromXmlResource("benchmarkConfig.xml");
		benchmarkFactory.buildPlannerBenchmark().benchmark();


		
	
		
		//benchmarkFactory.getPlannerBenchmarkConfig().getInheritedSolverBenchmarkConfig().getProblemBenchmarksConfig().setSolutionFileIOClass((Class<SolutionFileIO>) Class.forName("com.tracktik.scheduler.benchmark.SchedulerSolutionFileIO"));
		//benchmarkFactory.getPlannerBenchmarkConfig().getInheritedSolverBenchmarkConfig().getProblemBenchmarksConfig().setInputSolutionFileList(inputSolutionFileList);
		

		

	}

}
