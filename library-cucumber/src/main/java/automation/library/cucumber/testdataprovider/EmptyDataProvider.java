package automation.library.cucumber.testdataprovider;

import java.util.Map;


public class EmptyDataProvider implements TestDataList {
    @Override
    public Object get(int i) {
        return null;
    }

    @Override
    public Map<String, Object> getMapOfElement(int i) {
        return null;
    }
}
