package step_implementations;

import cucumber.api.DataTable;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.with;

public class BasicStepImplementation {

    public String basicApplicationURL = null;
    public String headerParamRequestContentType = null;
    public String headerParamResponseAcceptType = null;
    public String basicVersion = null;
    private RequestSpecification requestspecs = null;
    private Response responseJayWay = null;
    public String currentAPIEndPoint = null;
    private String authHeaderValue = null;
    private int responseCode = -1;
    private boolean isWebAppRunning = false;
    private boolean isQueryParamAdded = false;
    private boolean isBasicHeadersAdded = false;

    public void generateAuthHeaderKey(DataTable dataTable) {
        Map<String, String> authHeaderMap = dataTable.asMap(String.class, String.class);

        String apiKey = authHeaderMap.get("apikey");
        String secret = authHeaderMap.get("secret");
        Date date = new java.util.Date();
        Long timestamp = (date.getTime() / 1000);
        String signature = null;
        try {
            if (StringUtils.isNotEmpty(apiKey) || StringUtils.isNotEmpty(secret)) {
                String toBeHashed = apiKey + secret + timestamp;
                MessageDigest md = MessageDigest.getInstance("SHA-512");
                byte[] bytes = md.digest(toBeHashed.getBytes("UTF-8"));
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < bytes.length; i++) {
                    sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
                }
                signature = sb.toString();
                authHeaderValue = "EAN APIKey=" + apiKey + ",Signature=" + signature + ",timestamp=" + timestamp;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void parseBasicHeaders(DataTable basicHeaders) {
        requestspecs = setup_Connection();
        Map<String, String> basicHeaderMap = basicHeaders.asMap(String.class, String.class);
        requestspecs.headers(basicHeaderMap);
        if (authHeaderValue != null) {
            requestspecs.header("Authorization", authHeaderValue);
            isBasicHeadersAdded = true;
        }
    }

    public void parseBasicQueryParams(DataTable basicQueryParams) {
        Map<String, String> basicQueryParamMap = basicQueryParams.asMap(String.class, String.class);
        requestspecs.queryParams(basicQueryParamMap);
        isQueryParamAdded = true;
    }


    public void raiseHTTPMethod(String httpMethod) {
        switch (httpMethod) {
            case "GET":
                try {
                    responseJayWay = requestspecs.get(basicApplicationURL + "/" + basicVersion + currentAPIEndPoint);
                } catch (Exception e) {
                    if (e.getMessage().contains("Connection refused")) {
                        Assert.fail("Web application is not running.");
                        return;
                    } else {
                        Assert.fail("Unknown exception while invoking this request.");
                        e.printStackTrace();
                    }
                    return;
                }
                break;
            default:
                Assert.fail("This type of method is not supported yet.");
        }
    }

    private RequestSpecification setup_Connection() {
        RequestSpecification requestspecs = with();
        //    requestspecs.contentType(headerParamRequestContentType);
        //    requestspecs.accept(headerParamResponseAcceptType);
        return requestspecs;
    }

    public void checkIfAppRunning() {
        try {
            responseJayWay = requestspecs.get(basicApplicationURL);
        } catch (Exception e) {
            if (e.getMessage().contains("Connection refused")) {
                //System.out.println("Web application is not running.");
                Assert.fail("ERROR :Web application is not running. So cannot test functionality.");
                return;
            } else {
                System.out.println("Unknown exception while invoking this request.");
                e.printStackTrace();
            }
        }
        isWebAppRunning = true;
    }

    public void checkIfHeaderNotPresent(String header) {
        if (authHeaderValue != null)
            Assert.fail("AuthHeader is not null");
    }

    public void checkIfReqdQueryParamsAdded() {
        if (!isQueryParamAdded)
            Assert.fail("Basic Query Params Not Added ");
    }

    public void validateHTTPresponseCode(int expectedHttpResponseCode) {
        responseCode = responseJayWay.getStatusCode();
        if (responseCode != expectedHttpResponseCode) {
            Assert.fail("Expected response code : " + expectedHttpResponseCode +
                    " and actual response code : " + responseCode +
                    " are not matching");
        }
    }

    public void validateResponseBodyForInvalidAuth(DataTable expectedResponseTable) {
        //Map<String,String> responseMap = response.asMap(String.class,String.class);
        Map<String, String> actualResponseMap = responseJayWay.getBody().as(Map.class);
        String actualResponseHeader1 = actualResponseMap.get("type");
        String actualResponseHeader2 = actualResponseMap.get("message");
        String expectedResponseHeader1 = expectedResponseTable.asMap(String.class, String.class).get("type");
        String expectedResponseHeader2 = expectedResponseTable.asMap(String.class, String.class).get("message");
        Assert.assertTrue(expectedResponseHeader1.equals(actualResponseHeader1) && expectedResponseHeader2.equals(actualResponseHeader2));
    }

    public void checkIfInvalidVersionAdded() {
        List<String> versionList = new ArrayList<>();
        versionList.add("2");
        versionList.add("2.1");
        versionList.add("2.2");
        if (versionList.contains(basicVersion)) {
            // Should we go for throwing exceptions rather???
            Assert.fail("Invalid version is not provided");
        }
    }

    public void validateResponseBodyForInvalidVersion(DataTable expectedResponseTable) {
        Map<String, String> actualResponseMap = responseJayWay.getBody().as(Map.class);
        String actualResponseHeader1 = actualResponseMap.get("type");
        String actualResponseHeader2 = actualResponseMap.get("message");
        String expectedResponseHeader1 = expectedResponseTable.asMap(String.class, String.class).get("type");
        String expectedResponseHeader2 = expectedResponseTable.asMap(String.class, String.class).get("message");
        Assert.assertTrue(expectedResponseHeader1.equals(actualResponseHeader1) && expectedResponseHeader2.equals(actualResponseHeader2));

    }

    public void checkIfBasicsHeadersAreAdded() {
        if (!isBasicHeadersAdded) {
            Assert.fail("ERROR: All basic headers are not added.");
        }
    }
}



