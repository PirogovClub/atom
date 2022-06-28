package automation.library.cucumber.testdataprovider;

import java.util.List;
import java.util.Map;

public interface TestDataList<T,K> {


    default int getTestDataSize() {
        return 0;
    }

    default List<T> getTestDataList() {
        return null;
    }

    T get(int i) ;

    Map<String,K> getMapOfElement(int i);
}
