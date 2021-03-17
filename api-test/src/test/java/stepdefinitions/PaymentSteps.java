package stepdefinitions;

import com.payee.test.models.PaymentRequest;
import com.payee.test.utils.dao.AccountRepository;
import com.payee.test.utils.dao.PaymentRepository;
import com.payee.test.utils.dao.entity.AccountEntity;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Steps;
import org.assertj.core.api.SoftAssertions;
import org.springframework.beans.factory.annotation.Autowired;
import servicesimpl.PaymentApi;
import servicesimpl.ResponseAssertion;

import javax.xml.bind.ValidationException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

import static com.payee.test.utils.DtoUtils.removeFromDto;

public class PaymentSteps {
    public static final String DEFAULT_CREATOR_IBAN = "NL000000000000002";
    public static final String DEFAULT_DEBTOR_IBAN = "NL000000000000001";
    public static final String DEFAULT_AMOUNT = "1";

    private static final SoftAssertions softAssert = new SoftAssertions();

    @Steps
    PaymentApi paymentApi;

    @Steps
    ResponseAssertion responseAssertion;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    AccountRepository accountRepository;

    @When("I (try to )create a payment with the following parameters:")
    public void createPaymentWithDataTable(Map<String, String> transaction) {
        PaymentRequest payload = new PaymentRequest();
        payload.setTransactionId(transaction.getOrDefault("transactionId", generateUniqueTransactionId()));
        payload.setAmount(BigDecimal.valueOf(Long.parseLong(transaction.getOrDefault("amount", DEFAULT_AMOUNT))));
        payload.setDebtorIban(transaction.getOrDefault("debtorIban", DEFAULT_DEBTOR_IBAN));
        payload.setCreditorIban(transaction.getOrDefault("creditorIban", DEFAULT_CREATOR_IBAN));

        paymentApi.createPayment(payload);
    }

    @When("I try to request")
    public void blabla() {
        PaymentRequest paymentRequest = paymentApi.generateDefaultPaymentRequest();

        try {
            AccountEntity debtor = accountRepository.findById(DEFAULT_DEBTOR_IBAN).orElseThrow(() -> new ValidationException("Debtor doesnt't exist."));
            paymentRequest.setAmount(debtor.getBalance().add(BigDecimal.ONE));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        paymentApi.createPayment(paymentRequest);
    }

    @When("I try to create a new payment without '{word}' parameter")
    public void createPaymentWithoutMandatoryField(String field) {
        PaymentRequest paymentRequest = paymentApi.generateDefaultPaymentRequest();
        paymentApi.createPayment(removeFromDto(paymentRequest, field));
    }

    @When("I try to create a payment with {string} creator iban")
    public void tryToCreateTransactionWithInvalidCreator(String creatorIban) {
        PaymentRequest payload = new PaymentRequest();
        payload.setTransactionId(generateUniqueTransactionId());
        payload.setAmount(BigDecimal.valueOf(1));
        payload.setDebtorIban("NL000000000000001");
        payload.setCreditorIban(creatorIban);

        paymentApi.createPayment(payload);
    }

    public String generateUniqueTransactionId() {
        return String.format("TXN-%s", (new Timestamp(System.currentTimeMillis())).getTime());
    }
}
