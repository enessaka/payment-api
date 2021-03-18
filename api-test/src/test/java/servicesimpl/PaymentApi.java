package servicesimpl;

import com.payee.test.models.PaymentRequest;
import io.restassured.specification.RequestSpecification;
import net.thucydides.core.annotations.Step;
import org.springframework.util.NumberUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static com.payee.test.utils.restassured.Requests.post;
import static com.payee.test.utils.restassured.RestAssuredFactory.getRestClient;
import static stepdefinitions.PaymentSteps.*;

public class PaymentApi {
    private final RequestSpecification reqSpec = getRestClient();
    private static final String PAYMENT_ENDPOINT = "payment";

    @Step("Create a new payment")
    public void createPayment(PaymentRequest payload) {
        post(reqSpec, PAYMENT_ENDPOINT, payload);
    }

    @Step("Try to create payment without mandatory field")
    public void createPayment(Object invalidPayload) {
        post(reqSpec, PAYMENT_ENDPOINT, invalidPayload);
    }

    @Step("Create default payment payload")
    public PaymentRequest generateDefaultPaymentRequest() {
        return new PaymentRequest()
                .setAmount(NumberUtils.parseNumber(DEFAULT_AMOUNT, BigDecimal.class))
                .setCreditorIban(DEFAULT_CREATOR_IBAN)
                .setDebtorIban(DEFAULT_DEBTOR_IBAN)
                .setTransactionId(generateUniqueTransactionId());
    }

    @Step("Generate Unique Transaction Id")
    public String generateUniqueTransactionId() {
        return String.format("TXN-%s", (new Timestamp(System.currentTimeMillis())).getTime());
    }
}
