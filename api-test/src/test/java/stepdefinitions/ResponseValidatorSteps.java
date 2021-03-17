package stepdefinitions;

import cucumber.api.java.en.Then;
import net.thucydides.core.annotations.Steps;
import org.springframework.http.HttpStatus;
import servicesimpl.ResponseAssertion;

public class ResponseValidatorSteps {

    @Steps
    ResponseAssertion responseAssertion;

    @Then("I should receive status code '{word}'")
    public void assertStatusCode(String statusCodeText) {
        int expectedStatusCode = HttpStatus.valueOf(statusCodeText).value();
        responseAssertion.assertStatusCode(expectedStatusCode);
    }

    @Then("I should receive status code '{word}' with response contains {string} value")
    public void assertStatusCodeWithResponseBody(String statusCodeText, String text) {
        assertStatusCode(statusCodeText);
        responseAssertion.assertResponsePathContains(text);
    }

    @Then("the response should contain {string} value")
    public void validateResponseBody(String text) {
        responseAssertion.assertResponsePathContains(text);
    }
}
