package automation.library.dbUtils.http;

import automation.library.common.Property;
import automation.library.common.TestContext;
import org.apache.commons.configuration.PropertiesConfiguration;

import static automation.library.dbUtils.Constants.DB_UTILS_PROPERTIES;

public interface RequestsUrl {

    static PropertiesConfiguration getProperties() {
        return Property.getProperties(DB_UTILS_PROPERTIES);
    }

    static String getTestWorkBook() {
        return (String) TestContext.getInstance().testdataGet("testWorkBook");
    }

    String getUrl();

    String getTestCaseNameUrlVar();

    public static class Constants {
        public static final String APP_TO_CALL = "appToCall";
        public static final String MS_URI = "ms_uri";
        public static final String PROJECT_NAME = "projectName";
        public static final String TEST_CASE_NAME_URL_VAR = "testCaseNameUrlVar";
        public static final String COLUMN_NAME_URL_VAR = "columnNameUrlVar";
        public static final String COLUMN_VALUE_URL_VAR = "columnValueUrlVar";
        public static final String POLICY_ID_URL_VAR = "policyId";
        public static final String EXTRA_PARAM_LINE = "worksheet";
        public static final String WORKSHEET_URL_VAR_VALUE = "?worksheet=";

        public static final String MAX_ROW_AMOUNT = "maxRowsAmount";
    }
}
