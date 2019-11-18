package com.tracktik.test.steps;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/scale_fact_level.feature",
    plugin = {"pretty", "html:target/cucumber"},
    tags = {}
)
public class RunFeaturesTest { }
