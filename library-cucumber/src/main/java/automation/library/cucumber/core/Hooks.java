package automation.library.cucumber.core;

import automation.library.common.TestContext;
import io.cucumber.core.api.Scenario;
import io.cucumber.java8.En;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Hooks implements En{

	protected Logger log = LogManager.getLogger(this.getClass().getName());

	public Hooks() {
		Before(10, (Scenario scenario) -> {
			String featureName = (scenario.getId().split(";")[0].replace("-", " "));
			String scenarioName = scenario.getName();
			TestContext.getInstance().putFwSpecificData("fw.testDescription", featureName + "-" + scenarioName);
			TestContext.getInstance().putFwSpecificData("fw.cucumberTest", "true");
			TestContext.getInstance().resetSoftAssert();
			log.info("**********************************************************");
			log.info(featureName + " : " + scenarioName);
			log.info("**********************************************************");
			try{
				StepDataTestContext.getInstance().stepTestDataSetNull();
			}catch (Exception ignore){}
		});
	}
}
