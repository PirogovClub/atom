package runners;

import io.cucumber.testng.CucumberOptions;

@CucumberOptions(tags = {"@books", "not @ignore"})

public class ApiGoogle extends BaseRunner {}