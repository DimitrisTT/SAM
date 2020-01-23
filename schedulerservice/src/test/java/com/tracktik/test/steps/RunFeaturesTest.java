package com.tracktik.test.steps;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/",
    plugin = {"pretty", "html:target/cucumber"},
    tags = {}
)
public class RunFeaturesTest { }
