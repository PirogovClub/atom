package automation.library.dbUtils.http;

import automation.library.common.AtomException;
import automation.library.common.TestContext;
import automation.library.dbUtils.dataTable.WorkSheetRow;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

@Log4j2
public class ApiClient {

    public static final String PARAM_KEY_TABLE_NAME = "tableName";
    public static final String PARAM_KEY_WORKSHEET = "worksheet";
    public static final String PARAM_KEY_TABLE_ROW_DATA = "tableRowData";

    @Getter
    @Setter
    private class RequestResponse {
        String responseBody;
        String responseCode;
        String responseHTTPDescription;
    }

    //param Map should be replace with builder
    public static JSONArray sendRequest(Map<String, Object> paramMap, RequestsUrls requestsUrl) {
        JSONArray data_obj = null;
        String urlToHit = "";

        try {
            if (paramMap.containsKey("testCaseName")) {
                urlToHit = requestsUrl.getUrl()
                        + "?" + PARAM_KEY_WORKSHEET + "=" + encodeValue((String) paramMap.get(PARAM_KEY_WORKSHEET))
                        + requestsUrl.getTestCaseNameUrlVar()
                        + encodeValue((String) paramMap.get("testCaseName"));
            } else {
                urlToHit = requestsUrl.getUrl();
            }
            switch (requestsUrl) {
                case getColumnNames:
                    urlToHit = requestsUrl.getUrl()
                            + "?" + PARAM_KEY_WORKSHEET + "=" + encodeValue((String) paramMap.get(PARAM_KEY_WORKSHEET));
                    break;
                default:
                    break;

            }

            /*
              to be able to receive big json response we need to change buffer in memory size from
              default 256k to 16MB
             */
            WebClient webClient = WebClient
                    .create(urlToHit)
                    .mutate()
                    .codecs(configurer -> configurer
                            .defaultCodecs()
                            .maxInMemorySize(16 * 1024 * 1024))
                    .build();

            Mono<String> responses;
            TestContext context = TestContext.getInstance();
            switch (requestsUrl) {

                case postColumnValue:

                    WorkSheetRow excelRow = new WorkSheetRow();
                    excelRow.setWorksheet((String) paramMap.get(PARAM_KEY_WORKSHEET));

                    excelRow.setColumnValues((Map<String, String>) paramMap.get(PARAM_KEY_TABLE_ROW_DATA));


                    responses = webClient
                            .post()
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(excelRow))
                            .exchangeToMono(response -> {
                                if (response.statusCode()
                                        .equals(HttpStatus.OK)) {
                                    return response.bodyToMono(String.class);
                                } else if (response.statusCode()
                                        .is4xxClientError()) {
                                    return Mono.just("Error response");
                                } else {
                                    return response.createException()
                                            .flatMap(Mono::error);
                                }
                            });
                    break;
                default:
                    responses = webClient.get().exchangeToMono(response -> {
                        if (response.statusCode()
                                .equals(HttpStatus.OK)) {

                            return response.bodyToMono(String.class);
                        } else if (response.statusCode()
                                .is4xxClientError()) {
                            return Mono.just("Error response: " + response.statusCode() + response.bodyToMono(String.class));
                        } else {
                            return response.createException()
                                    .flatMap(Mono::error);
                        }
                    });

            }
            final String responsesBlock = responses.block();
            if (!responsesBlock.contains("Error response")) {
                //Using the JSON simple library parse the string into a json object
                //TODO: fix bubble gum solution to return array
                data_obj = new JSONArray();
                JSONObject tmpJson;

                switch (requestsUrl) {

                    case getColumnNames:
                        tmpJson = (JSONObject) (new JSONParser()).parse(responsesBlock);
                        tmpJson = (JSONObject) tmpJson.get("currentColumnNames");
                        data_obj.add(tmpJson);
                        break;
                    case postColumnValue:
                        data_obj.add((new JSONParser()).parse(responsesBlock));
                        break;
                    default:
                        data_obj.add((new JSONParser()).parse(responsesBlock));
                }
            } else {
                throw new AtomException(responsesBlock);
            }

        } catch (WebClientException e) {
            throw new AtomException("Can't connect to microservice", e);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return data_obj;
    }


    private static String getResponse(URL url) throws IOException {
        String inline = "";
        try (Scanner scanner = new Scanner(url.openStream())) {

            //Write all the JSON data into a string using a scanner
            while (scanner.hasNext()) {
                inline += scanner.nextLine();
            }
        } catch (Exception e) {
            throw e;
        }
        return inline;
    }

    private static String encodeValue(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }

    public static JSONArray postDataToTable(String tableName, RequestsUrls requestsUrl) {
        JSONArray data_obj = null;

        try {


            WebClient webClient = WebClient.create(requestsUrl.getUrl() + encodeValue(tableName));

            Mono<String> responses = webClient.get().exchangeToMono(response -> {
                if (response.statusCode()
                        .equals(HttpStatus.OK)) {
                    return response.bodyToMono(String.class);
                } else if (response.statusCode()
                        .is4xxClientError()) {
                    return Mono.just("Error response");
                } else {
                    return response.createException()
                            .flatMap(Mono::error);
                }
            });
            if (!responses.equals("Error response")) {
                //Using the JSON simple library parse the string into a json object
                data_obj = (JSONArray) (new JSONParser()).parse(responses.block());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data_obj;
    }

}
