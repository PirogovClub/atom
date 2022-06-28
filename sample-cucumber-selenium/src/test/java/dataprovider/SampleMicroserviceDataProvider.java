package dataprovider;

import automation.library.common.AtomException;
import automation.library.cucumber.testdataprovider.TestDataList;
import automation.library.dbUtils.dataTable.SingleTestDataRow;
import automation.library.dbUtils.dataTable.SingleTestDataRowImpl;
import automation.library.dbUtils.dataTable.WorkSheetRow;
import automation.library.dbUtils.http.ApiClient;
import automation.library.dbUtils.http.RequestsUrls;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;


@Log4j2
public class SampleMicroserviceDataProvider implements TestDataList<SingleTestDataRow,HashMap<String, String>> {


    final List<SingleTestDataRow> dataList;

    public SampleMicroserviceDataProvider() {
        dataList = getDataFromMicroservice();
    }

    @Override
    public List<SingleTestDataRow> getTestDataList() {
        return dataList;
    }

    @Override
    public int getTestDataSize() {
        return dataList.size();
    }

    @Override
    public SingleTestDataRow get(int i) {

        //CHECK CONVERTION
        return dataList.get(i);
    }

    @Override
    public Map<String, HashMap<String, String>> getMapOfElement(int i){
        return get(i).getTableDataRow();

    }


    //list of worksheets with one in each of them
    private List<SingleTestDataRow> getDataFromMicroservice() {

        List<SingleTestDataRow> testDataRowHashMap = new ArrayList<>();
        JSONArray testCaseDataRows = ApiClient.sendRequest(new HashMap<>(), RequestsUrls.getAllTestCaseDataRows);
        //Something strange do not expect to have Array in the arrat
        testCaseDataRows = (JSONArray) testCaseDataRows.get(0);

        for (Object testCaseDataRow : testCaseDataRows) {
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

    private SingleTestDataRow prepareTestDataRow(JSONObject dataRow) {

        SingleTestDataRow testDataRow = new SingleTestDataRowImpl();
        Map<String, HashMap<String, String>> tableDataRowMap = new HashMap<>();
        JSONObject workbookDataRow = (JSONObject) dataRow.get("WorkbookDataRows");
        testDataRow.setWorkBookName(dataRow.get("WorkbookName").toString());

        for (Object tableName : workbookDataRow.keySet()) {
            HashMap<String, String> columnNames = new HashMap<>();
            HashMap<String, String> resultLine = new HashMap<>();
            WorkSheetRow workSheetRow = new WorkSheetRow();
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
            final JSONObject currentWorksheetData = (JSONObject) workbookDataRow.get(tableName);

            currentWorksheetData.keySet().forEach(keyStr -> resultLine.put(
                    columnNames.get(keyStr.toString()),
                    currentWorksheetData.get(keyStr) == null ? "" : currentWorksheetData.get(keyStr).toString()));

            tableDataRowMap.put(tableName.toString(), resultLine);

        }
        testDataRow.setTableDataRow(tableDataRowMap);
        return testDataRow;
    }
}
