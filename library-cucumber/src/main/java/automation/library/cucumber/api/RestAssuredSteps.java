package automation.library.cucumber.api;

import automation.library.api.core.*;
import automation.library.common.Property;
import automation.library.common.TestContext;
import automation.library.cucumber.core.Constants;
import automation.library.cucumber.core.StepDataTestContext;
import com.fasterxml.jackson.core.JsonParseException;
//import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.filter.Filter;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static automation.library.common.JsonHelper.getJSONData;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * Common Steps class which invokes the RestAssuredHelper class and enables
 * rest api calls to be configured and invoked from cucumber features along
 * with detailed response validation.
 */
public class RestAssuredSteps {
    RestContext restContext;
    RequestSpecification request = given();
    protected Logger log = LogManager.getLogger(this.getClass().getName());
    private String baseSoapXML = null; //TODO- SOAP

    public RestAssuredSteps(RestContext restContext) {
        this.restContext = restContext;
    }

    JSONObject basedata = null;
    private String api = null;
    private String path = null;
    private String method = null;

    private static boolean apiTest;

    public static boolean isApiTest() {
        return apiTest;
    }

    public static void setApiTest(boolean api) {
        apiTest = api;
    }

    public void resetRest() {
        request = given();
        restContext.getRestData().setRequest(request);
    }

    @Given("^a rest api \"(.*)\"$")
    public void setAPI(String api) {
        restContext.getRestData().setRequest(request);
        this.api = api;
        String envURI = Property.getProperty(Constants.ENVIRONMENTPATH + Property.getVariable("cukes.env") + ".properties", api);
        RestAssuredHelper.setBaseURI(restContext.getRestData().getRequest(), envURI);

        setApiTest(true);
    }

    @Given("^(challenged basic|basic) authorisation$")
    public void setBasicAuth(String type) {
        String[] auth = Property.getProperty(Constants.ENVIRONMENTPATH + Property.getVariable("cukes.env") + ".properties", api + ".basicauth").split(":");
        if (type.equalsIgnoreCase("basic")) {
            RestAssuredHelper.setBasicAuth(restContext.getRestData().getRequest(), auth[0], auth[1]);
        } else {
            RestAssuredHelper.setChallengedBasicAuth(restContext.getRestData().getRequest(), auth[0], auth[1]);
        }
    }

    @Given("^a base uri \"(.*)\"$")
    public void setBaseURI(String uri) {
        RestAssuredHelper.setBaseURI(restContext.getRestData().getRequest(), uri);
    }

    @Given("^a base path \"(.*)\"$")
    public void setBasePath(String basepath) {
        RestAssuredHelper.setBasePath(restContext.getRestData().getRequest(), basepath);
    }

    @Given("^a port (\\d+)$")
    public void setPort(int port) {
        RestAssuredHelper.setPort(restContext.getRestData().getRequest(), port);
    }

    @Given("^a header$")
    public void setHeader(Map<String, String> map) {
        map.forEach((key, val) -> {
            String value = patternSearchAndEvaluate(val);
            RestAssuredHelper.setHeader(restContext.getRestData().getRequest(), key, value);
            if (key.equalsIgnoreCase("Content-Type")) restContext.getRestData().setContextType(value);
        });
    }

    @Given("^(form parameters|query parameters|path parameters|parameters)$")
    public void withParams(String type, Map<String, String> map) {
        map.forEach((key, val) -> {
            boolean list = false;
            List<String> vals = new ArrayList<>();
            String v = patternSearchAndEvaluate(val);
            if (v.contains("::")) {
                list = true;
                vals = Arrays.asList(v.split("::"));
            }

            if (!list)
                RestAssuredHelper.setParam(restContext.getRestData().getRequest(), type, key, v);
            else
                RestAssuredHelper.setParamList(restContext.getRestData().getRequest(), key, vals);
        });
    }

    @And("multipart")
    public void multipart(List<Map<String, String>> list) {
        list.forEach(map -> {
            if (map.size() == 1) {
                String filepath = automation.library.api.core.Constants.BASEPATH + "testdata/inputs/" + map.get("file");
                RestAssuredHelper.setMultiPart(restContext.getRestData().getRequest(), new File(filepath));
            } else if (map.size() == 2) {
                String controlName = map.get("controlName");
                if (map.containsKey("file")) {
                    String filepath = automation.library.api.core.Constants.BASEPATH + "testdata/inputs/" + map.get("file");
                    RestAssuredHelper.setMultiPart(restContext.getRestData().getRequest(), controlName, new File(filepath));

                } else if (map.containsKey("contentBody")) {
                    RestAssuredHelper.setMultiPart(restContext.getRestData().getRequest(), controlName, map.get("contentBody"));
                }
            } else if (map.size() == 3) {
                String controlName = map.get("controlName");
                String mimeType = map.get("mimeType");
                if (map.containsKey("file")) {
                    String filepath = automation.library.api.core.Constants.BASEPATH + "testdata/inputs/" + map.get("file");
                    RestAssuredHelper.setMultiPart(restContext.getRestData().getRequest(), controlName, new File(filepath), mimeType);

                } else if (map.containsKey("contentBody")) {
                    RestAssuredHelper.setMultiPart(restContext.getRestData().getRequest(), controlName, map.get("contentBody"), mimeType);
                }
            }
        });
    }


    @Given("^base input data \"([^\"]*)\"$")
    public void setBaseInputData(String arg1) {
        if (arg1.substring(0, 2).equalsIgnoreCase("<<")) {
            String[] str = arg1.replace("<<", "").replace(">>", "").split("\\.");
            String testdatafile = str[0];
            String testdataset = str[1];
            String path = "src/test/resources/testdata/inputs/" + testdatafile + ".json";
            baseSoapXML = "src/test/resources/testdata/inputs/" + str[0] + ".xml";
            basedata = getJSONData(path, testdataset);
        } else {
            basedata = new JSONObject(arg1);
        }
    }

    @When("^a request body$")
    @Given("^a request body \"(.*)\"$")
    public void requestBody(String data) throws Exception {
      /*  String body;
        if (restContext.getRestData().getContextType().toLowerCase().contains("xml")) {
            if (data.substring(0, 2).equalsIgnoreCase("<<")) {
                String[] str = data.replace("<<", "").replace(">>", "").split("\\.");

                String testdatafile = str[0];
                String testdataset = null;
                if (str.length > 1) testdataset = str[1];

                String path = "src/test/resources/testdata/inputs/" + testdatafile + ".json";
                baseSoapXML = "src/test/resources/testdata/inputs/" + str[0] + ".xml";

                DocumentBuilderFactory dbf = new DocumentBuilderFactoryImpl();
                Document doc = dbf.newDocumentBuilder().parse(new File(baseSoapXML));
                //TODO - need to find way to merge the Json into XML. As of now not merging base xml to input body
                body = APIHelper.createRequestXML(doc);
            } else {
                body = data;
            }
        } else {
            if (!data.substring(0, 2).equalsIgnoreCase("<<")) {
                data = patternSearchAndEvaluate(data);
            }
            if (basedata == null) {
                body = RestAssuredHelper.createJson(data);
            } else {
                body = RestAssuredHelper.createJson(data, basedata);
            }
        }

        RestAssuredHelper.setBody(restContext.getRestData().getRequest(), body);*/
    }

    @Given("^Extract and Save \"(.*)\" from response$")
    public void extractAndSave(String jsonPathFromResponse) {
        String contentType = restContext.getRestData().getResponse().header("Content-Type");
        String responseString = restContext.getRestData().getRespString();
        String value = "";
        if (contentType.contains("application/json")) {
            JsonPath jsonPath = new JsonPath(responseString);
            value = jsonPath.get(jsonPathFromResponse).toString();
        } else if (contentType.contains("text/plain")) {
            value = responseString;
        }
        StepDataTestContext.getInstance().stepTestDataPut(jsonPathFromResponse, value);
    }

    @When("^the system requests (GET|PUT|POST|PATCH|DELETE) \"(.*)\"$")
    public void apiGetRequest(String apiMethod, String path) {
        path = patternSearchAndEvaluate(path);
        this.path = path;
        this.method = apiMethod;
        Response response = RestAssuredHelper.callAPI(restContext.getRestData().getRequest(), apiMethod, path);
        restContext.getRestData().setResponse(response);
        resetRest();        //enables multiple api calls within single scenario
    }

    @Then("^the response code is (\\d+)$")
    public void verify_status_code(int code) throws NumberFormatException {
        RestAssuredHelper.checkStatus(restContext.getRestData(), code);
        TestContext.getInstance().sa().assertAll();
    }

    @Then("^the response status is \"(.*)\"$")
    public void verify_status_message(String msg) throws NumberFormatException {
        RestAssuredHelper.checkStatus(restContext.getRestData(), msg);
        TestContext.getInstance().sa().assertAll();
    }

    @Then("^the response time is less than (\\d+) milliseconds$")
    public void verifyResponseTime(long duration) {
        RestAssuredHelper.checkResponseTime(restContext.getRestData(), duration);
    }

    @Then("^the response header contains$")
    public void verifyHeader(List<List<String>> list) {
        Object val;
        String key;
        String matcher;
        for (List<String> row : list) {
            if (row.size() == 2) {
                matcher = "equals";
                key = row.get(0);
                val = row.get(1);
            } else {
                matcher = row.get(1);
                key = row.get(0);
                val = row.get(2);
            }
            RestAssuredHelper.checkHeader(restContext.getRestData(), key, matcher, val);
        }
    }

    @And("^the response body is empty$")
    public void responseBodyEmpty() {
        if (!(restContext.getRestData().getRespString().equalsIgnoreCase("{}")
                || restContext.getRestData().getRespString().equalsIgnoreCase("[]"))) {
            fail("response body not empty....contains: " + restContext.getRestData().getRespString());
        }
    }

    @And("^the (json|response) body( strictly|) contains$")
    public void responseBodyValid(String type, String mode, DataTable table) throws IOException, JSONException {
        List<List<String>> temp = table.cells();

        String responseString;
        if (type.equalsIgnoreCase("response")) {
            responseString = restContext.getRestData().getRespString();
        } else {
            responseString = TestContext.getInstance().testdataGet("jsonBody").toString();
        }

        if (temp.get(0).size() == 1) {
            String[] filename = temp.get(1).get(0).replace("<<", "").replace(">>", "").split("\\.");
            RestAssuredHelper.responseBodyValid(mode, filename[0], filename[1], responseString);
        } else if (temp.get(0).size() == 2) {
            RestAssuredHelper.responseBodyValid(this.api, this.method, this.path, table.asMap(String.class, String.class), responseString);
        } else {
            RestAssuredHelper.responseContains(table.asList(ResponseValidator.class), responseString);
        }
    }


    @And("^the response matches the (json|xml) schema \"(.*)\"$")
    public void matchJSONSchema(String type, String path) {
        RestAssuredHelper.checkSchema(restContext.getRestData(), type, "testdata/schemas/" + path);
    }


    @And("^trace out request response$")
    public void traceOut() {
        System.out.println("request: " + restContext.getRestData().getRequestString());
        System.out.println("response: " + restContext.getRestData().getRespString());
    }

    @Given("^the (request|response|request and response) (?:satisfy|satisfies) the contract \"([^\"]*)\"$")
    public void requestResponseSatisfiesContract(String mode, String contract) {
        Filter validationFilter = new SwaggerSoftValidationFilter(mode, contract);
        request.filter(validationFilter);
    }

    @Given("^the (request|response|request and response) (?:satisfy|satisfies) the contract$")
    public void requestResponseSatisfiesContract(String mode) {
        String contract = Property.getProperty(Constants.ENVIRONMENTPATH + Property.getVariable("cukes.env") + ".properties", String.join(".", api, "Contract"));
        requestResponseSatisfiesContract(mode, contract);
    }

    @And("^wait (.*) milliseconds for request to process$")
    public void WaitDevice(long time) throws InterruptedException {
        Thread.sleep(time);
    }

    @And("^response body does not contain \"(.*)\"$")
    public void ResponseDoesNotContain(String path) {
        String response = restContext.getRestData().getRespString();
        JSONObject obj = new JSONObject(response);
        Assert.assertTrue(obj.isNull(path));
    }

    @Given("set urlEncodingEnabled to false")
    public void urlEncodingEnabled() {
        restContext.getRestData().getRequest().urlEncodingEnabled(false);
    }


    @And("assign (.*) = (.*)")
    public void assignVar(String var, String val) {
        String v = patternSearchAndEvaluate(val);
        StepDataTestContext.getInstance().stepTestDataPut(var, v);
        log.info(var + " = " + v);
    }

    private String patternSearchAndEvaluate(String str) {
        String s = "<$";
        String e = "$>";

        int i = str.indexOf(s);

        int j = 1;
        Map<String, Integer> map = new HashMap<>();
        while (i >= 0) {
            map.put("s" + j, i);
            j++;
            i = str.indexOf(s, i + 1);
        }

        i = str.indexOf(e);
        j = 1;
        while (i >= 0) {
            map.put("e" + j, i);
            j++;
            i = str.indexOf(e, i + 1);
        }

        ///checking if proper closure of <$....$> is done.
        if (map.size() % 2 == 1) {
            log.info("Error: Incorrect closure of expression");
        }

        //making sure that we have got the pattern to evaluate
        if ((map.size() % 2 == 0) && (map.size() != 0)) {
            final Map<String, Integer> smap = map.entrySet()
                    .stream()
                    .sorted((Map.Entry.<String, Integer>comparingByValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

            Set<String> keys = smap.keySet();

            ArrayList<String> groups = new ArrayList();
            while (!keys.isEmpty()) {
                Object[] x = keys.toArray();

                for (int m = 0; m < x.length - 1; m++) {
                    if (!x[m].toString().substring(0, 1).
                            equalsIgnoreCase(
                                    x[m + 1].toString().substring(0, 1))
                    ) {
                        groups.add(str.substring(map.get(x[m]) + 2, map.get(x[m + 1])));
                        keys.remove(x[m]);
                        keys.remove(x[m + 1]);
                        break;
                    }
                }
            }

            for (int y = 0; y < groups.size(); y++) {
                String exp = groups.get(y);
                String value = stepDataFindReplace2(exp);

                for (int z = 0; z < groups.size(); z++) {
                    String oldExp = groups.get(z);
                    String newExp = oldExp.replace("<$" + exp + "$>", value);
                    str = str.replace("<$" + exp + "$>", value);
                    groups.set(z, newExp);

                }
            }
        }
        return str;
    }

    private String stepDataFindReplace2(String val) {
        boolean changed = false;
        String reportMessage = val;
        String jsonElement;
        String valFromRes = "";
        if (val.startsWith("response.")) {
            jsonElement = val.replace("response.", "");

            String contentType = restContext.getRestData().getResponse().header("Content-Type");
            String responseString = restContext.getRestData().getRespString();

            if (contentType.contains("json")) {
                try {
                    JsonPath jsonPath = new JsonPath(responseString);
                    valFromRes = jsonPath.get(jsonElement).toString();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("assigning the entire response");
                    valFromRes = responseString;
                }

            } else if (contentType.contains("text/plain")) {
                valFromRes = responseString;
            }
            changed = true;
        } else {
            if (StepDataTestContext.getInstance().getStepTestData().containsKey(val)) {
                valFromRes = StepDataTestContext.getInstance().stepTestDataGet(val).toString();
                changed = true;
            } else {
                //get from input json file //TODO
            }
        }

        if (changed) log.info("processed: " + reportMessage + " = " + valFromRes);
        return valFromRes;
    }
}