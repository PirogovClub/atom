package automation.library.dbUtils.http;

import lombok.Getter;

import java.util.AbstractMap;
import java.util.Map;

import static automation.library.dbUtils.http.RequestsUrl.Constants.*;


public enum RequestsUrls implements RequestsUrl {

    getAll(
            Map.ofEntries(
                    new AbstractMap.SimpleEntry<>(APP_TO_CALL, "ms_get_all_records"),
                    new AbstractMap.SimpleEntry<>(MS_URI, (Constants.MS_URI))
            )
    ),
    getOneRow(
            Map.ofEntries(
                    new AbstractMap.SimpleEntry<>(APP_TO_CALL, "ms_get_one_row"),
                    new AbstractMap.SimpleEntry<>(MS_URI, (MS_URI)),
                    new AbstractMap.SimpleEntry<>(TEST_CASE_NAME_URL_VAR, "&test_case_id=")
            )
    ),
    getUnExecuted(
            Map.ofEntries(
                    new AbstractMap.SimpleEntry<>(APP_TO_CALL, "ms_get_unexecuted"),
                    new AbstractMap.SimpleEntry<>(MS_URI, (MS_URI))
            )
    ),
    getColumnNames(
            Map.ofEntries(
                    new AbstractMap.SimpleEntry<>(APP_TO_CALL, "ms_get_column_names"),
                    new AbstractMap.SimpleEntry<>(MS_URI, (MS_URI))
            )
    ),
    getTableNames(
            Map.ofEntries(
                    new AbstractMap.SimpleEntry<>(APP_TO_CALL, "ms_get_column_names"),
                    new AbstractMap.SimpleEntry<>(MS_URI, (MS_URI))
            )
    ),
    getAllPoliciesByPolicyId(
            Map.ofEntries(
                    new AbstractMap.SimpleEntry<>(APP_TO_CALL, "ms_get_all_policies_by_policy_id"),
                    new AbstractMap.SimpleEntry<>(MS_URI, (MS_URI)),
                    new AbstractMap.SimpleEntry<>(POLICY_ID_URL_VAR, "&policyId=")
            )
    ),
    postColumnValue(
            Map.ofEntries(
                    new AbstractMap.SimpleEntry<>(APP_TO_CALL, "ms_set_column_value"),
                    new AbstractMap.SimpleEntry<>(MS_URI, (MS_URI))
            )
    ),
    getAllTestCaseDataRows(
            Map.ofEntries(
                    new AbstractMap.SimpleEntry<>(APP_TO_CALL, "ms_all_test_case_data_rows"),
                    new AbstractMap.SimpleEntry<>(MS_URI, (MS_URI))
            )
    ),
    getDataForAllUnExecutedTestCase(
            Map.ofEntries(
                    new AbstractMap.SimpleEntry<>(APP_TO_CALL, "ms_all_test_case_data_rows"),
                    new AbstractMap.SimpleEntry<>(MS_URI, (MS_URI))
            )
    ),
    getDataForXXUnExecutedTestCase(
            Map.ofEntries(
                    new AbstractMap.SimpleEntry<>(APP_TO_CALL, "ms_all_test_case_data_rows"),
                    new AbstractMap.SimpleEntry<>(MS_URI, (MS_URI)),
                    new AbstractMap.SimpleEntry<>(EXTRA_PARAM_LINE, "?maxRows="),
                    new AbstractMap.SimpleEntry<>(MAX_ROW_AMOUNT, "ms_max_rows_in_unexecuted")
            )
    ),
    getDataForOneUnExecutedTestCase(
            Map.ofEntries(
                    new AbstractMap.SimpleEntry<>(APP_TO_CALL, "ms_all_test_case_data_rows"),
                    new AbstractMap.SimpleEntry<>(MS_URI, (MS_URI)),
                    new AbstractMap.SimpleEntry<>(EXTRA_PARAM_LINE, "?maxRows=1")
            )
    );


    @Getter
    private final String url;
    private final String ms_uri;
    private final String projectName;
    private final String appToCall;
    @Getter
    private final String testCaseNameUrlVar;

    @Getter
    private final String policyIdUrlVar;
    private final String maxRowsAmount;


    private RequestsUrls(Map<String, String> paramSet) {

        this.ms_uri =
                paramSet.get(MS_URI) == null ? "" : RequestsUrl.getProperties().getString(paramSet.get(MS_URI));
        this.projectName =
                paramSet.get(PROJECT_NAME) == null ? "" : RequestsUrl.getProperties().getString(paramSet.get(PROJECT_NAME));
        this.appToCall =
                paramSet.get(APP_TO_CALL) == null ? "" : RequestsUrl.getProperties().getString(paramSet.get(APP_TO_CALL));
        this.testCaseNameUrlVar =
                paramSet.getOrDefault(TEST_CASE_NAME_URL_VAR, null);
        this.policyIdUrlVar =
                paramSet.get(POLICY_ID_URL_VAR) == null ? "" : RequestsUrl.getProperties().getString(paramSet.get(POLICY_ID_URL_VAR));
        this.maxRowsAmount =
                paramSet.get(MAX_ROW_AMOUNT) == null ? "" : RequestsUrl.getProperties().getString(paramSet.get(MAX_ROW_AMOUNT));


        //Need to convert this to builder
        this.url = this.ms_uri
                + "/" + RequestsUrl.getProperties().getString("ms_app_name")
                + "/"
                + RequestsUrl.getProperties().getString(paramSet.get(APP_TO_CALL))
                + paramSet.getOrDefault(EXTRA_PARAM_LINE, "")
                + this.maxRowsAmount;

    }

}

