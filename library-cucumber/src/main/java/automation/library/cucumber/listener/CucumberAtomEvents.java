package automation.library.cucumber.listener;

import automation.library.common.listeners.AtomEventSimple;
import lombok.Getter;

public enum CucumberAtomEvents implements AtomEventSimple {

    BUTTON_CLICK_OK ( "Button clicked successfully"),
    BUTTON_CLICK_FAIL ( "Button click fail");

    @Getter
    private String message;
    CucumberAtomEvents(String s) {
        message=s;
    }

    public String toString(){
        return getMessage();
    }
}
