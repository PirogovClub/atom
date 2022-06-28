package runners;

import automation.library.cucumber.selenium.RunnerClassParallel;
import automation.library.cucumber.selenium.RunnerClassSequential;
//import automation.library.reporting.TextReport;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterTest;


@CucumberOptions(
        plugin = {"automation.library.reporting.adapter.ExtentCucumberAdapter:"},
        features = {"classpath:features"},
        glue = {"steps", "hooks"}

        )

public class BaseRunner extends RunnerClassParallel {

    //TestNG after hook
  /*  @AfterTest
    public void teardown() {
        TextReport tr = new TextReport();
        tr.createReport(true);
    }

   */
}