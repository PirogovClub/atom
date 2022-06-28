package automation.library.dbUtils.dataTable;

/**
 * Represent
 */

public interface TableDataRow {
    String getWorksheet();

    java.util.Map<String, String> getColumnValues();

    void setWorksheet(String workSheetName);

    void setColumnValues(java.util.Map<String, String> columnValues);
}
