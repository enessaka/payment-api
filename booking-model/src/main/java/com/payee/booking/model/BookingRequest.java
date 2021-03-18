package com.payee.booking.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@Data
@XmlRootElement(name = "BookingRequest")
public class BookingRequest {

    String transactionId;
    String debtorIban;
    String debtorAccountManagementSystem;
    String creditorIban;
    String creditorAccountManagementSystem;
    BigDecimal amount;

}
