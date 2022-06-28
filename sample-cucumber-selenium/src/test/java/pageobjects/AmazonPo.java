package pageobjects;

import automation.library.selenium.core.Element;
import automation.library.selenium.core.ExtendedBy;
import automation.library.selenium.exec.BasePO;
import org.openqa.selenium.By;

public class AmazonPo extends BasePO {


    public AmazonPo() {
        log.debug("in constructor");
    }

    final ExtendedBy searchBar = new ExtendedBy(
            By.xpath("//input[@id=\"twotabsearchtextbox\"]"),
            "Search input box");
    final By firstSearchResult = By.xpath("//h1[contains(text(),\"Search Result\")]/following-sibling::div[1]/div[1]//h3");

    final By iphoneTitle = By.xpath("//span[contains(text(),'Apple iPhone 13 (256GB) - Midnight')]");
    final By iphonePriceIn = By.xpath("//parent::a//parent::h2//parent::div//following-sibling::div//span[contains(text(),'80,990')][1]");
    final By iphoneTitleLink = By.xpath("//span[contains(text(),'Apple iPhone 13 (256GB) - Midnight')]//parent::a");



    public void sendToSearch(String textToSend) {
        $(searchBar.setImpactingData(textToSend)).click().sendKeys(textToSend).sendEnter();
    }

    public boolean checkIfOnfOn1stLine(String textToCheck) {
        By firsResultWithText = By.xpath("//h1[contains(text(),\"Search Result\")]/following-sibling::div[1]/div[1]//h3[contains(text(),\"" + textToCheck + "\")]");
        return this.exist(firsResultWithText);

    }

    public void findAllNeededphones() {
        for (Element element : $$(iphoneTitle)) {
            Element tmpEl = element.$(iphonePriceIn);
            if (tmpEl != null) {
                element.move();
                $(iphoneTitleLink).click().waitPageToLoad();

            }

        }
    }

    public String getFirstResultText() {
        return $(firstSearchResult).getText();
    }

}
