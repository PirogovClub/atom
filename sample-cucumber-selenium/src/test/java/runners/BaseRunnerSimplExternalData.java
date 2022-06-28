package runners;

import automation.library.cucumber.selenium.RunnerClassParallel;
import automation.library.reporting.PDFReport;
import automation.library.reporting.Reporter;
import dataprovider.SampleSimpleDataProvider;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterTest;

import java.io.IOException;

@CucumberOptions(
        plugin = {"automation.library.reporting.adapter.ExtentCucumberAdapter:"},
        features = {"classpath:features"},
        glue = {"steps", "hooks"}
)

public class BaseRunnerSimplExternalData extends RunnerClassParallel {
    public BaseRunnerSimplExternalData() {
        super(new SampleSimpleDataProvider());

    }

       /* @AfterTest
        public void teardown() throws IOException {
            PDFReport.scenarioPDFReport(Reporter.getReportFilePath());
        }*/
}