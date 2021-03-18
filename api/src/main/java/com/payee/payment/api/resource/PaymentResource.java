package com.payee.payment.api.resource;

import com.payee.payment.api.error.ValidationException;
import com.payee.payment.api.resource.model.Transaction;
import com.payee.payment.api.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PaymentResource {

    @Autowired
    PaymentService service;

    @PostMapping("payment")
    public ResponseEntity performBooking(@RequestBody Transaction transaction){
       try {
           service.execute(transaction);
           return ResponseEntity.ok().build();
       } catch (ValidationException e) {
           log.error("Validation Error", e);
           return ResponseEntity.badRequest().body(e.getMessage());
       } catch (Exception e) {
           log.error("Internal Error", e);
           return ResponseEntity.status(500).build();
       }
    }
}
