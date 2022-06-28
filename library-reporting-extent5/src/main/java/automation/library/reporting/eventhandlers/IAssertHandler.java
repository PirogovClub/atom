package automation.library.reporting.eventhandlers;

import automation.library.common.listeners.AtomEventSimple;
import automation.library.common.listeners.CommonAtomEvents;
import org.testng.asserts.IAssert;

import static automation.library.reporting.Reporter.*;
import static automation.library.reporting.Reporter.addInfoToStepLog;

public class IAssertHandler implements ReporterEventHandler{

    public static void handleEvent(AtomEventSimple event, IAssert targetObject) {
        String message = (targetObject.getMessage() == null ? "" : " with message [" + targetObject.getMessage() + "]");
        String statusString = "[" + event + "]"
                + message
                + " expected "
                + "[" + targetObject.getExpected() + "]"
                + " get"
                + "[" + targetObject.getActual() + "]";
        if (CommonAtomEvents.ASSERT_ASSERTION_ERROR.equals(event) ||
                CommonAtomEvents.ASSERT_ASSERTION_FAIL.equals(event)) {
            statusString = CROSS_MARK + statusString;
            addFailToStepLog(statusString);
        } else if (CommonAtomEvents.ASSERT_ASSERTION_SUCCESS.equals(event)) {
            statusString = SLIGHTLY_SMILING_FACE + statusString;
            addInfoToStepLog(statusString);
        }
    }
}
