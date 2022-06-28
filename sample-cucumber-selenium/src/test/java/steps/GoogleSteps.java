package steps;

import automation.library.common.TestContext;
import automation.library.cucumber.selenium.BaseSteps;
import automation.library.dbUtils.dataTable.SingleTestDataRow;
import automation.library.reporting.Reporter;
import automation.library.selenium.exec.PoInit;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pageobjects.GooglePo;

import java.util.Map;

import static automation.library.selenium.exec.Constants.TEST_DATA_ARG_NAME;

public class GoogleSteps extends BaseSteps {

    //traditional instantiation on class load
    GooglePo googlePO = new GooglePo();

    //lazy PO instantiation would happen with 1st call
    PoInit<GooglePo> googlePo = new PoInit<>(){};

    @When("user search for {string}")
    public void userSearchForText(String var) {
        log.debug("in google step");

        googlePo.get().sendToSearch(var);
        googlePo.get().getFirstResultText();
    }

    @Then("first result has {string} in link")
    public void firstResultHasInLink(String arg0) {
        sa().assertTrue(googlePO.checkIfOnfOn1stLine(arg0));

    }


    @Then("first result has text From External Test data source in link")
    public void firstResultHasTextFromExternalTestDataSourceInLink() {
        Map<String, String> testData = (Map<String, String>) TestContext.getInstance().testdata().get(TEST_DATA_ARG_NAME);
        String textToValidate = testData.get("textToSearch");
        Reporter.addRunningScreenShot();
        sa().assertEquals(googlePO.getFirstResultText(), textToValidate);

    }

    @Then("first result has wrong text From External Test data source in link")
    public void firstResultHasWrongTextFromExternalTestDataSourceInLink() {
        Map<String, String> testData = (Map<String, String>) TestContext.getInstance().testdata().get(TEST_DATA_ARG_NAME);
        String textToValidate = testData.get("textToSearch") + "HelloWorld";
        sa().assertEquals(googlePO.getFirstResultText(), textToValidate);

    }


    @Then("first result has text From Microservice Test data source in link")
    public void firstResultHasTextFromMicroserviceTestDataSourceInLink() {
        SingleTestDataRow dataRow =
                (SingleTestDataRow) TestContext.getInstance().testdata().get(TEST_DATA_ARG_NAME);
        sa().assertEquals(googlePo.get().getFirstResultText(), dataRow.getTableDataRow().get("Iul Rider").get("Rider_Code"));
        sa().assertAll();
    }

    @Then("running assertion")
    public void runningAssertion() {
        sa().assertAll();
    }
}
