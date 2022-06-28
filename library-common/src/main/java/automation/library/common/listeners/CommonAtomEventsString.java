package automation.library.common.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Deprecated
public class CommonAtomEventsString implements AtomEventType {

    public final static String JSON_PARSED_OK = "Json Parsed successfully";
    public final static String FORMAT_HELPER_SSN_GENERATED = "Generate SSN";
    public final static String ASSERT_ASSERTION_SUCCESS = "Assert Pass";
    public final static String ASSERT_ASSERTION_FAIL = "Assert Fail";
    public static final String ASSERT_ASSERTION_ERROR = "Assert Error";

    public static List<String> getEventsAsList() {
        List<String> stringList = new ArrayList<>(Arrays.asList(
                JSON_PARSED_OK,
                FORMAT_HELPER_SSN_GENERATED,
                ASSERT_ASSERTION_SUCCESS,
                ASSERT_ASSERTION_FAIL,
                ASSERT_ASSERTION_ERROR

        ));
        return stringList;
    }
}
