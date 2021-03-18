package servicesimpl;

import net.thucydides.core.annotations.Step;

import static com.payee.test.utils.restassured.Requests.then;
import static org.hamcrest.Matchers.*;

public class ResponseAssertion {

    @Step("Check whether status code is as {0}")
    public void assertStatusCode(int statusCode) {
        then().statusCode(describedAs(String.format("as %s,", statusCode), is(statusCode)));
    }

    @Step("Check whether response contains {1}")
    public void assertResponsePathContains(String text) {
        then().body(containsString(text));
    }
}
