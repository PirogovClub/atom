package runners;

import automation.library.common.TestContext;
import automation.library.cucumber.listener.CucumberEventTypes;
import automation.library.cucumber.selenium.RunnerClassParallel;
import automation.library.reporting.Reporter;
import dataprovider.SampleMicroserviceDataProvider;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        plugin = {"automation.library.reporting.adapter.ExtentCucumberAdapter:"},
        features = {"classpath:features"},
        glue = {"steps", "hooks"}
        )

public class BaseRunnerSampleMicroservice extends RunnerClassParallel {
        public BaseRunnerSampleMicroservice() {
                super(new SampleMicroserviceDataProvider());

        }

       /* @AfterTest
        public void teardown() {
                TextReport tr = new TextReport();
                tr.createReport(true);
        }*/
}