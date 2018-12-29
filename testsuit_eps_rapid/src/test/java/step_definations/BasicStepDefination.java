package step_definations;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import step_implementations.BasicStepImplementation;

public class BasicStepDefination {

    BasicStepImplementation handle = null;


    @Given("^simple init$")
    public void simple_init() {
        handle = new BasicStepImplementation();

    }

    @Given("^Generate authHeaderKey with$")
    public void generate_authHeaderKey_with(DataTable authKeys) {
        handle.generateAuthHeaderKey(authKeys);
    }

    @Given("^Basic web application endpoint url is \"(.*?)\"$")
    public void basic_web_application_endpoint_url_is(String basicApplicationURL) {
        handle.basicApplicationURL = basicApplicationURL;
    }

    @Given("^Basic version is \"(.*?)\"$")
    public void basic_version_is(String basicVersion) {
        handle.basicVersion = basicVersion;
    }

    @Given("^Basic headers are$")
    public void basic_headers_are(DataTable basicHeaders) {
        handle.parseBasicHeaders(basicHeaders);
    }

    @Given("^Basic queryParams are$")
    public void basic_queryParams_are(DataTable basicQueryParams) {

        handle.parseBasicQueryParams(basicQueryParams);
    }

    @Given("^Basic web application is running$")
    public void basic_web_application_is_running() {

        handle.checkIfAppRunning();
    }

    @When("^a user sets GET request to \"(.*?)\"$")
    public void a_user_sets_GET_request_to(String endPoint) {

        handle.currentAPIEndPoint = endPoint;

    }

    @When("^user adds Basic headers without header \"(.*?)\"$")
    public void user_adds_Basic_headers_without_header(String header) {

        handle.checkIfHeaderNotPresent(header);
    }

    @When("^user adds Basic headers$")
    public void user_adds_Basic_headers() {

        //TODO: To be implemented
        handle.checkIfBasicsHeadersAreAdded();
    }

    @When("^user adds Basic queryParams$")
    public void user_performs_GET_Basic_queryParams() {

        handle.checkIfReqdQueryParamsAdded();
    }

    @And("^performs GET request$")
    public void performs_GET_request() {
        handle.raiseHTTPMethod("GET");
    }

    @Then("^the response code should be (\\d+)$")
    public void the_response_code_should_be(int responseCode) {

        handle.validateHTTPresponseCode(responseCode);
    }

    @And("^user should see json response with pairs$")
    public void user_should_see_json_response_with_pairs(DataTable expectedResponseTable) {

        handle.validateResponseBody(expectedResponseTable);
    }
    @Then("^user should see json response for invalid resource$")
    public void user_should_see_json_response_for_invalid_resource(DataTable expectedResponseTable)  {
        handle.validateResponseBodyForInvalidResource(expectedResponseTable);
    }

    @When("^user adds invalid basic version$")
    public void user_adds_invalid_basic_version() {
        handle.checkIfInvalidVersionAdded();
    }

    @Then("^user should see json response for invalid version$")
    public void user_should_see_json_response_for_invalid_version(DataTable expectedResponseTable) {
        handle.validateResponseBodyForInvalidVersion(expectedResponseTable);
    }


}