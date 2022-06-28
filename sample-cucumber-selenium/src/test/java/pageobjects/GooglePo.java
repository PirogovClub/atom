package pageobjects;

import automation.library.selenium.core.ExtendedBy;
import automation.library.selenium.exec.BasePO;
import org.openqa.selenium.By;

public class GooglePo extends BasePO {



    public GooglePo(){
        log.debug("in constructor");
    }

    final ExtendedBy searchBar = new ExtendedBy(
            By.xpath("//input[@title='Search']"),
    "Search input box");
    final By firstSearchResult =By.xpath("//h1[contains(text(),\"Search Result\")]/following-sibling::div[1]/div[1]//h3");


    public void sendToSearch(String textToSend){
        $(searchBar.setImpactingData(textToSend)).click().sendKeys(textToSend).sendEnter();
    }

    public boolean checkIfOnfOn1stLine(String textToCheck){
        By firsResultWithText =By.xpath("//h1[contains(text(),\"Search Result\")]/following-sibling::div[1]/div[1]//h3[contains(text(),\""+textToCheck+"\")]");
        return this.exist(firsResultWithText);

    }

    public String getFirstResultText(){
        return $(firstSearchResult).getText();
    }

}
