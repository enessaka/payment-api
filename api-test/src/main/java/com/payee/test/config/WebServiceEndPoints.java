package com.payee.test.config;

public enum WebServiceEndPoints {
    PAYEE("http://localhost:8080/");

    private final String url;

    WebServiceEndPoints(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
