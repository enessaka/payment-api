package com.payee.payment.api.resource.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

import java.math.BigDecimal;

@Data
public class Transaction {

    String transactionId;

    String debtorIban;

    String creditorIban;

    BigDecimal amount;

}
