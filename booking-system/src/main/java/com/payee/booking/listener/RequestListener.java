package com.payee.booking.listener;

import com.payee.booking.model.BookingRequest;
import com.payee.booking.model.BookingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class RequestListener {

    @Autowired
    JmsTemplate jmsTemplate;

    @JmsListener(destination = "booking_request", containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(BookingRequest bookingRequest) {
        log.info("booking request:{}", bookingRequest);
        jmsTemplate.convertAndSend("booking_response", new BookingResponse(bookingRequest.getTransactionId(), 0, ""));
        log.info("booking request({}) is processed", bookingRequest.getTransactionId());
    }
}
