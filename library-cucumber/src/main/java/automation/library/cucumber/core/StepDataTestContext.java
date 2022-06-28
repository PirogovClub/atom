package automation.library.cucumber.core;

import java.util.HashMap;
import java.util.Map;

public class StepDataTestContext {

    private static StepDataTestContext SINGLE_INSTANCE = null;
    private Map<String, Object> stepTestData = null;

    public StepDataTestContext() {
    }


    public static synchronized StepDataTestContext getInstance(){
        if (SINGLE_INSTANCE == null) {
            synchronized (StepDataTestContext.class) {
                SINGLE_INSTANCE = new StepDataTestContext();
            }
        }
        return SINGLE_INSTANCE;
    }

    public Map<String, Object> getStepTestData() {
        return stepTestData;
    }

    public void setStepTestData(Map<String, Object> stepTestData) {
        this.stepTestData = stepTestData;
    }


    public Map<String, Object> stepTestData() {
        if (stepTestData == null)
            stepTestData = new HashMap<String, Object>();
        return stepTestData;
    }

    public Object stepTestDataGet(String key) {
        return stepTestData.get(key);
    }

    public void stepTestDataPut(String key, String data) {
        stepTestData().put(key, data);
    }

    public void stepTestDataClear() {
        if (!(stepTestData == null)) {
            stepTestData.clear();
        }
    }

    public void stepTestDataSetNull() {
        if (!(stepTestData == null)) {
            stepTestData = null;
        }
    }



}
