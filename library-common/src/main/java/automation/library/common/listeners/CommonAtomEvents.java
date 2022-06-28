package automation.library.common.listeners;

import lombok.Getter;

public enum CommonAtomEvents implements AtomEventSimple {

    JSON_PARSED_OK ( "Json Parsed successfully"),
    FORMAT_HELPER_SSN_GENERATED ( "Generate SSN"),
    ASSERT_ASSERTION_SUCCESS ( "Assert Pass"),
    ASSERT_ASSERTION_FAIL ( "Assert Fail"),
    ASSERT_ASSERTION_ERROR ( "Assert Error");

    @Getter
    private String message;
    CommonAtomEvents(String s) {
        message=s;
    }
    public String toString(){
        return getMessage();
    }
}
