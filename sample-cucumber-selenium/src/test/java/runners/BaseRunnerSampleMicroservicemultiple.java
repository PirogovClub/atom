package runners;

import automation.library.cucumber.selenium.RunnerClassParallel;
import dataprovider.SampleMicroserviceMultiRowDataProvider;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        plugin = {"automation.library.reporting.adapter.ExtentCucumberAdapter:"},
        features = {"classpath:features"},
        glue = {"steps", "hooks"}
        )

public class BaseRunnerSampleMicroservicemultiple extends RunnerClassParallel {
        public BaseRunnerSampleMicroservicemultiple() {
                super(new SampleMicroserviceMultiRowDataProvider());
        }

       /* @AfterTest
        public void teardown() {
                TextReport tr = new TextReport();
                tr.createReport(true);
        }*/
}