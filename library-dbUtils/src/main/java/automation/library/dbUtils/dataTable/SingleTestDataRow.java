package automation.library.dbUtils.dataTable;


import java.util.HashMap;
import java.util.Map;

/**
 * Represent one line from Excel workbook from all worksheets (TableDataRows)
 */

public interface SingleTestDataRow  {

    Map<String, HashMap<String, String>> getTableDataRow();
    void setTableDataRow(Map<String, HashMap<String, String>> TableDataRow);
    void setWorkBookName(String workBookName);
    String getWorkBookName();
}
