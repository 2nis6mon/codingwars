package com.dojocoders.codingwars.cucumber;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(strict = true, features = "classpath:features/piloteAutomatique.feature")
public class PiloteAutomatiqueCucumberTest {
	// See src/test/resources for cucumber scenarios
}
