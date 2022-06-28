package automation.library.reporting.eventhandlers;

import automation.library.common.listeners.AtomEventSimple;
import automation.library.selenium.core.Element;

import static automation.library.reporting.Reporter.ARROW_POINTING_DOWNWARDS_THEN_CURVING_RIGHTWARDS;
import static automation.library.reporting.Reporter.addInfoToStepLog;

public class ElementHandler implements ReporterEventHandler{

    public static void handleEvent(AtomEventSimple event, Element targetObject) {
        final String elementReportCaption =
                targetObject.getElementReportCaption().equals("")
                        ? targetObject.getStringOfBy()
                        : targetObject.getElementReportCaption();

        final String impactingData = targetObject.getImpactingData().equals("")
                ? "No driving data"
                : targetObject.getImpactingData();

        addInfoToStepLog(ARROW_POINTING_DOWNWARDS_THEN_CURVING_RIGHTWARDS + "'" + event + "'"
                + " on "
                + "'" + elementReportCaption + "'"
                + " with "
                + "'" + impactingData + "'"
                + " data");
    }
}
