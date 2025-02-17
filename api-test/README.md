## Getting started with Serenity and Cucumber

Serenity BDD is a library that makes it easier to write high-quality automated acceptance tests, with powerful reporting
and living documentation features. It has strong support for both web testing with Selenium, and API testing using
RestAssured.

### Findings

#### Issues

1. Although service.execute tries to update payments, the transaction was not able to commit. In order to fix that issue
   @Transactional annotation added to PaymentService
2. ResponseListener updates only payment status on payments table thus accounts' balances are not getting updated after
   success booking (api/booking/ResponseListener needs to update accounts' balances as well as status update)
3. There is a typo on the error message of non-existing account response
4. When the payment request is missing a field, api throws exception and returns `Internal Server Error` to the
   requester. Instead of that flow, a proper error code (BAD_REQUEST)/message (XXX must not be null) needs to be
   returned to the requester.

Additionally, in docker-compose.yml, default ports added to payment-db service in order to query database

```yml
 ports:
   - "3306:3306"
```

### Prerequisites

`JDK 11`
`Maven 3.6`

### Payment API

Usage Clone the repo with git clone git@github.com:enessaka/payment-api.git

All applications need to be installed

*ProjectPath* `mvn clean install -Dmaven.test.skip=true`

Spring-boot plugin's build:image task needs to be run for both api and booking-system applications

*Api and Booking-system package* `mvn spring-boot:build-image`

All containers need to be up and running

*misc/docker* `docker-compose up -d`
logs can be followed via `docker-compose logs -f` command

### The project directory structure

The project has build scripts for Maven, and follows the standard directory structure used in most Serenity projects:

```
src
+ main
    + java
        + com.payee.test
            + config
            + models
            + utils
+ test
    + java
        + servicesimpl
        + stepdefinitions
    + resources
        + features
        + logback-test.xml
        + serenity.conf 
  ```        

### The sample scenario

Both variations of the sample project uses the sample Cucumber scenario:

```Gherkin
@successPayment
Scenario: Success payment
When I create a new payment request
Then I should receive status code 'OK'
```

### Models

PaymentRequest model has been generated manually.

### Executing the tests

To run the api-test project, `mvn clean verify` command can be called via terminal.

### Test Coverage

This test code has covered both negative and positive scenarios. Of course, test coverage needs to be extended, but the
main idea is to how the test suite is future proof with respect to scalability and maintainability.

The glue code is responsible for orchestrating calls to a layer of more business-focused classes, which perform the
actual REST calls. Example of the Glue code:

```java
@When("I create a new payment request")
public void createPaymentRequest(){
        PaymentRequest paymentRequest=paymentApi.generateDefaultPaymentRequest();
        paymentApi.createPayment(paymentRequest);
        Serenity.setSessionVariable(TXN_ID).to(paymentRequest.getTransactionId());
        }
```

The actual REST calls are performed using RestAssured in the TrelloApi implementation:

```java
@Step("Create a new payment")
public void createPayment(PaymentRequest payload){
        post(reqSpec,PAYMENT_ENDPOINT,payload);
        }
```

### Test Results

Detailed HTML report could be found in local target folder

```
target/site/serenity/index.html
```
