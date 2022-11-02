package runners;

import io.cucumber.testng.CucumberOptions;

@CucumberOptions(tags = {"@createPet", "not @ignore"})

public class ApiCreatePet extends BaseRunner {}