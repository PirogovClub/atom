

package dataprovider;


        import java.util.Map;

/**
 * Represent one line from Excel workbook from all worksheets (TableDataRows)
 */

public interface MultiTestDataRow  {

    Map<String, Object> getTableDataRow();

    void setTableDataRow(Map<String, Object> TableDataRow);
    void setWorkBookName(String workBookName);
    String getWorkBookName();
}

