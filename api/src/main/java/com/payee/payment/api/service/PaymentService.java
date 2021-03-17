package com.payee.payment.api.service;

import com.payee.booking.model.BookingRequest;
import com.payee.payment.api.dao.AccountRepository;
import com.payee.payment.api.dao.PaymentRepository;
import com.payee.payment.api.dao.entity.AccountEntity;
import com.payee.payment.api.dao.entity.PaymentEntity;
import com.payee.payment.api.error.ValidationException;
import com.payee.payment.api.resource.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static com.payee.payment.api.Constants.ACCOUNT_ACTIVE;

@Transactional
@Service
public class PaymentService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    JmsTemplate template;

    public void execute(Transaction transaction) throws ValidationException {
        validateAmount(transaction);
        AccountEntity debtor = accountRepository.findById(transaction.getDebtorIban()).orElseThrow(() -> new ValidationException("Debtor doesnt't exist."));
        AccountEntity creditor = accountRepository.findById(transaction.getCreditorIban()).orElseThrow(() -> new ValidationException("Creditor doesnt't exist."));
        validate(debtor, creditor, transaction);
        paymentRepository.save(PaymentEntity.from(debtor, creditor, transaction));
        template.convertAndSend("booking_request", createBookingRequest(debtor, creditor, transaction));
    }

    private void validateAmount(Transaction transaction) throws ValidationException {
        if (transaction.getAmount().compareTo(BigDecimal.valueOf(1_000_000L)) > 0) {
            throw new ValidationException("Transaction with amount lower than 1000000 can be executed via api");
        }
    }

    private void validate(AccountEntity debtor, AccountEntity creditor, Transaction transaction) throws ValidationException {
        if (!debtor.getStatus().equalsIgnoreCase(ACCOUNT_ACTIVE)) {
            throw new ValidationException("Debtor account is not active");
        } else if (!creditor.getStatus().equalsIgnoreCase(ACCOUNT_ACTIVE)) {
            throw new ValidationException("Creditor account is not active");
        } else if (debtor.getBalance().compareTo(transaction.getAmount()) < 0) {
            throw new ValidationException("Debtor doesn't have enough balance");
        }
        Optional<PaymentEntity> duplicateTrx = paymentRepository.findById(transaction.getTransactionId());
        if (duplicateTrx.isPresent()) {
            throw new ValidationException("Transaction is already performed.");
        }
    }

    private BookingRequest createBookingRequest
            (AccountEntity debtor, AccountEntity creditor, Transaction transaction) {
        BookingRequest req = new BookingRequest();
        req.setTransactionId(transaction.getTransactionId());
        req.setCreditorAccountManagementSystem(creditor.getAccountManagementSystem());
        req.setCreditorIban(creditor.getIban());
        req.setDebtorAccountManagementSystem(debtor.getAccountManagementSystem());
        req.setDebtorIban(debtor.getIban());
        req.setAmount(transaction.getAmount());
        return req;
    }
}


