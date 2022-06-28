package automation.library.cucumber.selenium;

import automation.library.common.JsonHelper;
import automation.library.cucumber.testdataprovider.TestDataList;
import automation.library.selenium.exec.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.cucumber.testng.TestNGCucumberRunner;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;


import static automation.library.selenium.exec.Constants.STACK_MAP_ID_IN_ARGS;
import static automation.library.selenium.exec.Constants.TESTDATA_MAP_ID_IN_ARGS;

@Log4j2
public class DataProviderComposer {

    private TestNGCucumberRunner testNGCucumberRunner;


    private TestDataList testDataList;

    public DataProviderComposer(TestNGCucumberRunner testNGCucumberRunner, TestDataList testDataList) {
        this.testNGCucumberRunner = testNGCucumberRunner;
        this.testDataList = testDataList;
    }


    public Object[][] scenarios() {
        //list of scenario
        Object[][] scenariosList = dpScenario();
        //browser tech stack
        JSONArray jsonArr = dpTechStackJSON();
        //creating object with scenario and browser stack combination


        final int dataListLenghForLoops = testDataList.getTestDataSize() > 0 ? testDataList.getTestDataSize() : 1;
        final int totalRunsAmount = scenariosList.length
                * jsonArr.length()
                * dataListLenghForLoops;

        Object[][] obj = new Object[totalRunsAmount][4];
        Gson gson = new GsonBuilder().create();

        int k = 0;

        for (int j = 0; j < jsonArr.length(); j++) {
            JSONObject jsonObj = jsonArr.getJSONObject(j);
            @SuppressWarnings("unchecked")
            Map<String, String> map = gson.fromJson(jsonObj.toString(), Map.class);
            for (int m = 0; m < dataListLenghForLoops; m++) {

                for (int i = 0; i < scenariosList.length; i++) {
                    obj[k][0] = scenariosList[i][0];
                    obj[k][1] = scenariosList[i][1];
                    obj[k][STACK_MAP_ID_IN_ARGS] = map;
                    obj[k][TESTDATA_MAP_ID_IN_ARGS] =
                            testDataList.getTestDataSize() > 0 ? testDataList.get(m) : null;
                    k++;
                }
            }
        }
        return obj;
    }

    public Object[][] dpScenario() {
        if (testNGCucumberRunner == null) {
            return new Object[0][0];
        }

        return testNGCucumberRunner.provideScenarios();
    }

    public JSONArray dpTechStackJSON() {
        log.debug("spinning up parallel execution threads for multi browser testing");
        return JsonHelper.getJSONArray(Constants.SELENIUMSTACKSPATH);
    }
}
