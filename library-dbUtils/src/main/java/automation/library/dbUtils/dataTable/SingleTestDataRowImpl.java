package automation.library.dbUtils.dataTable;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class SingleTestDataRowImpl implements SingleTestDataRow {
    private Map<String, HashMap<String, String>> tableDataRow;
    private String workBookName;
}
