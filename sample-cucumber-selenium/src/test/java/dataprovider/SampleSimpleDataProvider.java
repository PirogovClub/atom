package dataprovider;

import automation.library.cucumber.testdataprovider.TestDataList;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SampleSimpleDataProvider implements TestDataList<Map<String, Object>, Object> {

    final List<Map<String, Object>> dataList = new ArrayList<>();

    public SampleSimpleDataProvider() {
        dataList.add(Map.ofEntries(
                new AbstractMap.SimpleEntry<>("textToSearch", "iphone 13")));

        /*dataList.add(Map.ofEntries(
                new AbstractMap.SimpleEntry<>("textToSearch", "Timofey Pirogov - Test Automation Architect Manager - LinkedIn")));*/

    }

    @Override
    public List<Map<String, Object>> getTestDataList() {
        return dataList;
    }

    @Override
    public int getTestDataSize() {
        return dataList.size();
    }

    @Override
    public Map<String, Object> get(int i) {
        return dataList.get(i);
    }

    @Override
    public Map<String, Object> getMapOfElement(int i) {
        Map<String, Object> mapToReturn;
        mapToReturn = this.get(i);
        return mapToReturn;
    }

}
