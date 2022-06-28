package automation.library.cucumber.listener;

import automation.library.common.listeners.CommonAtomEventsString;
import automation.library.selenium.listener.SeleniumAtomEvents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Deprecated
/**
 * Deprecated to old event storage model
 * Use CucumberAtomEvents
 */

public  class CucumberEventTypes extends CommonAtomEventsString {
   public static final String BUTTON_CLICK_OK = "Button clicked successfully";
    public static final String BUTTON_CLICK_FAIL = "Button click fail";


    public static List<String> getEventsAsList() {
        List<String> listToReturn = new ArrayList<>();
        listToReturn.addAll(Arrays.asList(
                BUTTON_CLICK_OK,
                BUTTON_CLICK_FAIL
        ));

        return listToReturn;
    }
}
