package automation.library.selenium.listener;

import automation.library.common.listeners.AtomEventSimple;
import automation.library.common.listeners.AtomEventType;
import automation.library.common.listeners.CommonAtomEvents;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Not the best way to init events linst because of the chain

/**
 * Remember to add new event to getEventsAsList
 */

public enum SeleniumAtomEvents implements AtomEventSimple {

    ELEMENT_CLICKED_OK ( "Element Clicked successfully"),
    SCREEN_SHOT_TAKEN ( "ScreenShotTaken"),
    ELEMENT_SEND_KEYS ( "SendKeysToTheElement");

    @Getter
    private String message;
    SeleniumAtomEvents(String s) {
        message=s;
    }
    public String toString(){
        return getMessage();
    }
}
