package com.payee.test.utils.restassured;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.serenitybdd.core.Serenity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Requests {
    private static final String LAST_RESPONSE = "lastResponse";

    public static ValidatableResponse post(RequestSpecification rs, String url, Object body) {
        ValidatableResponse response = rs.body(body).post(url).then();
        Serenity.setSessionVariable(LAST_RESPONSE).to(response);
        return response;
    }

    public static ValidatableResponse get(RequestSpecification rs, String url) {
        ValidatableResponse response = rs.get(url).then();
        Serenity.setSessionVariable(LAST_RESPONSE).to(response);
        return response;
    }

    public static ValidatableResponse put(RequestSpecification rs, String url, Object body) {
        ValidatableResponse response = rs.body(body).put(url).then();
        Serenity.setSessionVariable(LAST_RESPONSE).to(response);
        return response;
    }

    public static ValidatableResponse delete(RequestSpecification rs, String url) {
        ValidatableResponse response = rs.delete(url).then();
        Serenity.setSessionVariable(LAST_RESPONSE).to(response);
        return response;
    }

    public static ValidatableResponse then() {
        return Serenity.sessionVariableCalled(LAST_RESPONSE);
    }
}
