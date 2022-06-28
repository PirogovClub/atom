package dataprovider;

import automation.library.common.AtomException;
import automation.library.cucumber.testdataprovider.TestDataList;
import automation.library.dbUtils.http.ApiClient;
import automation.library.dbUtils.http.RequestsUrls;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;


@Log4j2
public class SampleMicroserviceMultiRowDataProvider implements TestDataList<MultiTestDataRow, Object> {


    public static final String TEST_SETTINGS_TABLE_NAME = "Test Settings";
    public static final String IUL_RIDER_TABLE_NAME = "Iul Rider";
    final List<MultiTestDataRow> dataList;

    public SampleMicroserviceMultiRowDataProvider() {
        dataList = getDataFromMicroservice();
    }

    @Override
    public List<MultiTestDataRow> getTestDataList() {
        return dataList;
    }

    @Override
    public int getTestDataSize() {
        return dataList.size();
    }

    @Override
    public MultiTestDataRow get(int i) {

        //CHECK CONVERTION
        return dataList.get(i);
    }

    @Override
    public Map<String, Object> getMapOfElement(int i) {
        return get(i).getTableDataRow();

    }


    //list of worksheets with one in each of them
    private List<MultiTestDataRow> getDataFromMicroservice() {

        List<MultiTestDataRow> testDataRowHashMap = new ArrayList<>();
        Map<String, Object> queryParam = new HashMap<>();
//        queryParam.put("tableName", "Iul Rider");
//        queryParam.put("policyId", "C8557184");
        JSONArray testCaseDataRows = ApiClient.sendRequest(queryParam, RequestsUrls.getAllTestCaseDataRows);
        //Something strange do not expect to have Array in the arrat
//        testCaseDataRows = (JSONArray) testCaseDataRows.get(0);

        JSONArray tmpResult = (JSONArray) testCaseDataRows.get(0);

        for (Object testCaseDataRow : tmpResult) {
            JSONObject oneRow = (JSONObject) testCaseDataRow;
            testDataRowHashMap.add(prepareTestDataRow(oneRow));
        }
        return testDataRowHashMap;
    }


    private List<String> getListOfTables() {
        List<String> listToReturn = new ArrayList<>();

        JSONArray tableNameresponse =
                ApiClient.sendRequest(new HashMap<>(), RequestsUrls.getTableNames);

        for (Object o : tableNameresponse) {
            listToReturn.add(
                    ((JSONObject) o).get("tableName").toString()
            );

        }

        return listToReturn;
    }

    private MultiTestDataRow prepareTestDataRow(JSONObject dataRow) {

    //    Map<String,Object> testDataRow = new HashMap<>();
        MultiTestDataRow testDataRow = new MultiTestDataRowImpl();
        //  Map<String, List<HashMap<String, String>>> tablelistDataRowMap = new HashMap<>();
        //  Map<String, HashMap<String, String>> tableDataRowMap = new HashMap<>();
        Map<String, Object> tableDataRowMap = new HashMap<>();
        List<Object> listtableRow = new ArrayList<>();

        JSONObject testSettingsTable = (JSONObject) dataRow.get(TEST_SETTINGS_TABLE_NAME);
        JSONArray iulRiderRows = (JSONArray) dataRow.get(IUL_RIDER_TABLE_NAME);


        for (Object tableName : dataRow.keySet()) {
            HashMap<String, String> columnNames = new HashMap<>();
            HashMap<String, String> resultLine = new HashMap<>();
            JSONArray columnNamesResponse
                    = ApiClient.sendRequest(
                    Map.ofEntries(new AbstractMap.SimpleEntry<>("tableName", tableName.toString())),
                    RequestsUrls.getColumnNames);

            if (columnNamesResponse.size() != 1) {
                throw new AtomException("Wrong result of ColumnNames for table : " + tableName);
            }

            final JSONObject columnNamesResponseRow = (JSONObject) columnNamesResponse.get(0);
            columnNamesResponseRow.keySet().forEach(
                    columnKeyStr -> {
                        if (columnNamesResponseRow.get(columnKeyStr) == null) {
                            throw new AtomException("Null Column Name");
                        } else {
                            columnNames.put(columnKeyStr.toString(), columnNamesResponseRow.get(columnKeyStr).toString());
                        }
                    }
            );

            switch (tableName.toString()) {
                case TEST_SETTINGS_TABLE_NAME:
                    JSONObject currentWorksheetData = (JSONObject) dataRow.get(tableName);
                    currentWorksheetData.keySet().forEach(keyStr -> {
                        resultLine.put(
                                columnNames.get(keyStr.toString()),
                                currentWorksheetData.get(keyStr) == null ? "" : currentWorksheetData.get(keyStr).toString());
                    });

                    tableDataRowMap.put(tableName.toString(), resultLine);
                    break;
                case IUL_RIDER_TABLE_NAME:
                    JSONArray currentIulRider = (JSONArray) dataRow.get(tableName);
                    List<HashMap<String, String>> iulTableRowsToReturn = new ArrayList<>();

                    for (Object jsonObject : currentIulRider) {
                        JSONObject runningIulRow = (JSONObject) jsonObject;
                        runningIulRow.keySet().forEach(keyStr -> {
                            resultLine.put(
                                    columnNames.get(keyStr.toString()),
                                    runningIulRow.get(keyStr) == null ? "" : runningIulRow.get(keyStr).toString());
                        });
                        iulTableRowsToReturn.add(resultLine);
                    }
                    tableDataRowMap.put(tableName.toString(), iulTableRowsToReturn);
                    break;
            }


        }

       testDataRow.setTableDataRow(tableDataRowMap);
        return testDataRow;
    }
}
