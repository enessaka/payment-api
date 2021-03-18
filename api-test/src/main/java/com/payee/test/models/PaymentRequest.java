package com.payee.test.models;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class PaymentRequest {
    String transactionId;
    String debtorIban;
    String creditorIban;
    BigDecimal amount;
}
