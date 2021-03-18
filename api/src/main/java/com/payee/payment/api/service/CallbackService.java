package com.payee.payment.api.service;

import com.payee.payment.api.resource.model.Callback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class CallbackService {

    private final String callbackURL;
    RestTemplate restTemplate = new RestTemplate();

    public CallbackService(){
        callbackURL = System.getenv("CALLBACK_URL");
    }

    @Retryable(maxAttempts = 5, value = Exception.class, backoff = @Backoff(delay = 300))
    public void execute(String transactionId, String status){
        HttpEntity<Callback> request = new HttpEntity<>(new Callback(transactionId, status));
        restTemplate.postForEntity(callbackURL, request, String.class);
        log.info("Callback is performed to {}", callbackURL);
    }
}
