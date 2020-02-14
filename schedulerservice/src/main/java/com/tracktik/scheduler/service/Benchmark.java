package com.tracktik.scheduler.service;

import java.util.ArrayList;
import java.util.List;

import org.optaplanner.benchmark.api.PlannerBenchmark;
import org.optaplanner.benchmark.api.PlannerBenchmarkFactory;
import org.optaplanner.benchmark.config.blueprint.SolverBenchmarkBluePrintConfig;

public class Benchmark {

  public static void main(String[] args) {

    PlannerBenchmarkFactory benchmarkFactory = PlannerBenchmarkFactory.createFromXmlResource("benchmarkConfig.xml");

    // Sets the benchmark blueprint in desired
    List<SolverBenchmarkBluePrintConfig> blueprintConfigs = benchmarkFactory.getPlannerBenchmarkConfig()
        .getSolverBenchmarkBluePrintConfigList();
    if (blueprintConfigs == null)
      blueprintConfigs = new ArrayList<>();
    SolverBenchmarkBluePrintConfig solverBenchmarkBluePrintConfig = new SolverBenchmarkBluePrintConfig();
    // solverBenchmarkBluePrintConfig.setSolverBenchmarkBluePrintType(SolverBenchmarkBluePrintType.HILL_CLIMBING);
    // blueprintConfigs.add(solverBenchmarkBluePrintConfig);
    benchmarkFactory.getPlannerBenchmarkConfig().setSolverBenchmarkBluePrintConfigList(blueprintConfigs);

    PlannerBenchmark benchmark = benchmarkFactory.buildPlannerBenchmark();
    benchmark.benchmark();

  }

}
