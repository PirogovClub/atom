package automation.library.selenium.core;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;


public class ExtendedBy {

    private By locator;


    private String reportCaption;
    private String impactingData;

    public ExtendedBy(By locator, String reportCaption, String impactingData){
        this.locator=locator;
        this.reportCaption=reportCaption;
        this.impactingData=impactingData;
    }
    public ExtendedBy(By locator, String reportCaption){
        this(locator,reportCaption, "");
    }

    public ExtendedBy(By locator){
        this(locator,"", "");
    }


    public By getLocator() {
        return locator;
    }

    public ExtendedBy setLocator(By locator) {
        this.locator = locator;
        return this;
    }

    public String getReportCaption() {
        return reportCaption;
    }

    public ExtendedBy setReportCaption(String reportCaption) {
        this.reportCaption = reportCaption;
        return this;
    }

    public String getImpactingData() {
        return impactingData;
    }

    public ExtendedBy setImpactingData(String impactingData) {
        this.impactingData = impactingData;
        return this;
    }

}
