package com.tracktik.scheduler.service;

import org.optaplanner.benchmark.api.PlannerBenchmark;
import org.optaplanner.benchmark.api.PlannerBenchmarkFactory;
import org.optaplanner.benchmark.config.blueprint.SolverBenchmarkBluePrintConfig;
import org.optaplanner.benchmark.config.blueprint.SolverBenchmarkBluePrintType;

import java.util.ArrayList;
import java.util.List;

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

    List<SolverBenchmarkBluePrintConfig> blueprintConfigs = benchmarkFactory.getPlannerBenchmarkConfig().getSolverBenchmarkBluePrintConfigList();
    if (blueprintConfigs == null) blueprintConfigs = new ArrayList<>();
    SolverBenchmarkBluePrintConfig solverBenchmarkBluePrintConfig = new SolverBenchmarkBluePrintConfig();
    solverBenchmarkBluePrintConfig.setSolverBenchmarkBluePrintType(SolverBenchmarkBluePrintType.EVERY_CONSTRUCTION_HEURISTIC_TYPE_WITH_EVERY_LOCAL_SEARCH_TYPE);
    blueprintConfigs.add(solverBenchmarkBluePrintConfig);
    benchmarkFactory.getPlannerBenchmarkConfig().setSolverBenchmarkBluePrintConfigList(blueprintConfigs);


    PlannerBenchmark benchmark = benchmarkFactory.buildPlannerBenchmark();
    benchmark.benchmark();

    //benchmarkFactory.getPlannerBenchmarkConfig().getInheritedSolverBenchmarkConfig().getProblemBenchmarksConfig().setSolutionFileIOClass((Class<SolutionFileIO>) Class.forName("com.tracktik.scheduler.benchmark.SchedulerSolutionFileIO"));
    //benchmarkFactory.getPlannerBenchmarkConfig().getInheritedSolverBenchmarkConfig().getProblemBenchmarksConfig().setInputSolutionFileList(inputSolutionFileList);

  }

}
