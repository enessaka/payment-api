package stepdefinitions;

import com.payee.test.models.PaymentRequest;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import servicesimpl.PaymentApi;
import servicesimpl.ResponseAssertion;

import java.math.BigDecimal;
import java.util.Map;

import static com.payee.test.utils.DtoUtils.removeFromDto;

public class PaymentSteps {
    public static final String DEFAULT_CREATOR_IBAN = "NL000000000000002";
    public static final String DEFAULT_DEBTOR_IBAN = "NL000000000000001";
    public static final String DEFAULT_AMOUNT = "1";
    public static final String TXN_ID = "txnId";

    private static final SoftAssertions softAssert = new SoftAssertions();

    @Steps
    PaymentApi paymentApi;

    @Steps
    ResponseAssertion responseAssertion;

    //    @Autowired
    //    PaymentRepository paymentRepository;
    //
    //    @Autowired
    //    AccountRepository accountRepository;

    @Given("payment is created")
    public void ensurePaymentCreated() {
        if (!Serenity.hasASessionVariableCalled(TXN_ID)) {
            createPaymentRequest();
            responseAssertion.assertStatusCode(HttpStatus.SC_OK);
        }
    }

    @When("I try create payment with the same transaction id")
    public void duplicatePayment() {
        PaymentRequest paymentRequest = paymentApi.generateDefaultPaymentRequest();
        paymentRequest.setTransactionId(Serenity.sessionVariableCalled(TXN_ID));
        paymentApi.createPayment(paymentRequest);
    }

    @When("I create a new payment request")
    public void createPaymentRequest() {
        PaymentRequest paymentRequest = paymentApi.generateDefaultPaymentRequest();
        paymentApi.createPayment(paymentRequest);
        Serenity.setSessionVariable(TXN_ID).to(paymentRequest.getTransactionId());
    }

    @When("I (try to )create a payment with the following parameters:")
    public void createPaymentWithDataTable(Map<String, String> transaction) {
        PaymentRequest payload = new PaymentRequest()
                .setTransactionId(transaction.getOrDefault("transactionId", paymentApi.generateUniqueTransactionId()))
                .setAmount(BigDecimal.valueOf(Long.parseLong(transaction.getOrDefault("amount", DEFAULT_AMOUNT))))
                .setDebtorIban(transaction.getOrDefault("debtorIban", DEFAULT_DEBTOR_IBAN))
                .setCreditorIban(transaction.getOrDefault("creditorIban", DEFAULT_CREATOR_IBAN));

        paymentApi.createPayment(payload);
    }
    //
    //    @When("I try to request")
    //    public void blabla() {
    //        PaymentRequest paymentRequest = paymentApi.generateDefaultPaymentRequest();
    //
    //        try {
    //            AccountEntity debtor = accountRepository.findById(DEFAULT_DEBTOR_IBAN).orElseThrow(() -> new ValidationException("Debtor doesnt't exist."));
    //            paymentRequest.setAmount(debtor.getBalance().add(BigDecimal.ONE));
    //        } catch (ValidationException e) {
    //            e.printStackTrace();
    //        }
    //        paymentApi.createPayment(paymentRequest);
    //    }

    @When("I try to create a new payment without '{word}' parameter")
    public void createPaymentWithoutMandatoryField(String field) {
        PaymentRequest paymentRequest = paymentApi.generateDefaultPaymentRequest();
        paymentApi.createPayment(removeFromDto(paymentRequest, field));
    }
}
