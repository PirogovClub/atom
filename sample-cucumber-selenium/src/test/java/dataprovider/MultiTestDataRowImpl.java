package dataprovider;

import java.util.Map;

public class MultiTestDataRowImpl implements MultiTestDataRow{
    private Map<String, Object> tableDataRow;
    private String workBookName;
    @Override
    public Map<String, Object> getTableDataRow() {
        return this.tableDataRow;
       // return null;
    }

    @Override
    public void setTableDataRow(Map<String, Object> TableDataRow) {
        this.tableDataRow = tableDataRow;
    }

    @Override
    public void setWorkBookName(String workBookName) {
        this.workBookName = workBookName;
    }

    @Override
    public String getWorkBookName() {
        return this.workBookName;
       // return null;
    }
}
