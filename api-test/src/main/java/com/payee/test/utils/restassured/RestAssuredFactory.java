package com.payee.test.utils.restassured;

import com.payee.test.config.WebServiceEndPoints;
import io.restassured.RestAssured;
import io.restassured.config.HeaderConfig;
import io.restassured.config.ParamConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.SerenityRest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestAssuredFactory {
    private static final HeaderConfig HEADER_CONFIG = new HeaderConfig().overwriteHeadersWithName("Content-Type");
    private static final ParamConfig PARAM_CONFIG = new ParamConfig().replaceAllParameters();
    private static final ContentType DEFAULT_CONTENT_TYPE = ContentType.JSON;

    /**
     * It returns request specification from Serenity variables if it was already created, otherwise creates new.
     *
     * @return - request specification from Serenity variables if it was already created, otherwise creates new
     */
    public static RequestSpecification getRestClient() {
        String serviceSerenityKey = "PAYEE_API";

        if (Serenity.hasASessionVariableCalled(serviceSerenityKey)) {
            return Serenity.sessionVariableCalled(serviceSerenityKey);
        } else {
            RequestSpecification requestSpecification = SerenityRest
                    .given()
                    .config(RestAssured.config().headerConfig(HEADER_CONFIG).paramConfig(PARAM_CONFIG))
                    .contentType(DEFAULT_CONTENT_TYPE)
                    .baseUri(WebServiceEndPoints.PAYEE.getUrl());

            Serenity.setSessionVariable(serviceSerenityKey).to(requestSpecification);

            return requestSpecification;
        }
    }
}
