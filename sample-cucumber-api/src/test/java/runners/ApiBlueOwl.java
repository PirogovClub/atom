package runners;

import io.cucumber.testng.CucumberOptions;

@CucumberOptions(tags = {"@BlueOwl", "not @ignore"})

public class ApiBlueOwl extends BaseRunner {}