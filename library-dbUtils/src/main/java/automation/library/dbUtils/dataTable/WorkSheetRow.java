package automation.library.dbUtils.dataTable;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;


@Getter
@Setter

public class WorkSheetRow implements TableDataRow {
    private String workbook;
    private String worksheet;
    private Map<String, String> columnValues;
}


