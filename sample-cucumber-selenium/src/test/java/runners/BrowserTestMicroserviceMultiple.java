package runners;

import io.cucumber.testng.CucumberOptions;

//@CucumberOptions(tags = {"@donothing", "not @ignore"})
@CucumberOptions(tags = {"@amazon", "not @ignore"})
//@CucumberOptions(tags = {"@registerinterest,@journeyplanner,@donothing", "not @ignore"})
//@CucumberOptions(tags = {"@openGoogle", "not @ignore"})

public class BrowserTestMicroserviceMultiple extends BaseRunnerSampleMicroservicemultiple {}