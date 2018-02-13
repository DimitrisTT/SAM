package com.tracktik.scheduler.service;

import org.optaplanner.benchmark.api.PlannerBenchmark;
import org.optaplanner.benchmark.api.PlannerBenchmarkFactory;
import org.optaplanner.benchmark.config.blueprint.SolverBenchmarkBluePrintConfig;
import org.optaplanner.benchmark.config.blueprint.SolverBenchmarkBluePrintType;

import java.util.ArrayList;
import java.util.List;

public class Benchmark {

  public static void main(String[] args) {

    PlannerBenchmarkFactory benchmarkFactory = PlannerBenchmarkFactory.createFromXmlResource("benchmarkConfig.xml");

    List<SolverBenchmarkBluePrintConfig> blueprintConfigs = benchmarkFactory.getPlannerBenchmarkConfig().getSolverBenchmarkBluePrintConfigList();
    if (blueprintConfigs == null) blueprintConfigs = new ArrayList<>();
    SolverBenchmarkBluePrintConfig solverBenchmarkBluePrintConfig = new SolverBenchmarkBluePrintConfig();
    solverBenchmarkBluePrintConfig.setSolverBenchmarkBluePrintType(SolverBenchmarkBluePrintType.EVERY_CONSTRUCTION_HEURISTIC_TYPE_WITH_EVERY_LOCAL_SEARCH_TYPE);
    blueprintConfigs.add(solverBenchmarkBluePrintConfig);
    benchmarkFactory.getPlannerBenchmarkConfig().setSolverBenchmarkBluePrintConfigList(blueprintConfigs);

    PlannerBenchmark benchmark = benchmarkFactory.buildPlannerBenchmark();
    benchmark.benchmark();

  }

}
