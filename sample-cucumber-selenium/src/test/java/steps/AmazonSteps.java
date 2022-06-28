package steps;

import automation.library.cucumber.selenium.BaseSteps;
import automation.library.reporting.Reporter;
import automation.library.selenium.exec.PoInit;
import io.cucumber.java.en.When;
import pageobjects.AmazonPo;
import pageobjects.GooglePo;

public class AmazonSteps extends BaseSteps {

    AmazonPo amazonPo = new AmazonPo();

    //lazy PO instantiation would happen with 1st call
    PoInit<GooglePo> googlePo = new PoInit<>() {
    };

    @When("user search on Amazon for {string}")
    public void searchOnAmazonFor(String string) {
        amazonPo.sendToSearch(string);
    }

    @When("user clicks on 1st found with conditions")
    public void userClicksOnTheFirstFound() {
        amazonPo.findAllNeededphones();
       // Reporter.addRunningScreenShot();
    }

    @When("create screenshot for the report")
    public void createAScreenShot() {
        Reporter.addRunningScreenShot();
        // Reporter.addRunningScreenShot();
    }

}
