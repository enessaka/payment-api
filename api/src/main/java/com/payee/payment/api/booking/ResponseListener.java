package com.payee.payment.api.booking;

import com.payee.booking.model.BookingResponse;
import com.payee.payment.api.Constants;
import com.payee.payment.api.dao.PaymentRepository;
import com.payee.payment.api.dao.entity.PaymentEntity;
import com.payee.payment.api.service.CallbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class ResponseListener {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    CallbackService callbackService;

    @JmsListener(destination = "booking_response", containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(BookingResponse bookingResponse) {
        log.info("booking response:{}", bookingResponse);
        Optional<PaymentEntity> paymentOpt = paymentRepository.findById(bookingResponse.getTransactionId());
        if (paymentOpt.isEmpty()){
            log.error("Error while processing booking response, paymentOpt doesn't exists");
        }
        PaymentEntity payment = paymentOpt.get();
        if (bookingResponse.getCode() == 0) {
            payment.setStatus(Constants.PAYMENT_ACCEPTED);
            paymentRepository.save(payment);
        } else {
            payment.setStatus(Constants.PAYMENT_REJECTED);
            paymentRepository.save(payment);
        }

        callbackService.execute(bookingResponse.getTransactionId(), (bookingResponse.getCode() == 0) ? "A" : "R");

        log.info("booking response({}) is processed", bookingResponse.getTransactionId());
    }
}
