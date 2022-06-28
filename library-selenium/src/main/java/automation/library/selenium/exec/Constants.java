package automation.library.selenium.exec;


import automation.library.common.Property;


public class Constants extends automation.library.selenium.core.Constants{
    /**
     * POJO used to define constants for parallel execution in selenium test
     */
    public static final String SELENIUMSTACKSPATH = BASEPATH + "config/selenium/stacks/" + Property.getVariable("cukes.techstack") + ".json";
    public static final String TESTCASEPATH = BASEPATH + "scripts/testcases.xlsx";
    /**
     * Defining parameters places in arg[] for running test
     */
    public static final int STACK_MAP_ID_IN_ARGS = 2;
    public static final int TESTDATA_MAP_ID_IN_ARGS =3;
    public static final String TEST_DATA_ARG_NAME = "TestData";
    public static final String TEST_CASE_NAME_FROM_DATA = "TestCaseNameFromData";
}
