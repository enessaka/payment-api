@paymentApi
Feature: Payment API

  # bug - balance needs to be updated after a success booking confirmation
  # api/booking/ResponseListener needs to update accounts' balances as well as status update
  @successPayment
  Scenario: Success payment
    When I create a new payment request
    Then I should receive status code 'OK'

  @retryTransaction
  Scenario: Do payment with an existing transaction id
    Given payment is created
    When I try create payment with the same transaction id
    Then I should receive status code 'BAD_REQUEST' with response contains "Transaction is already performed" value

  # bug - There is a typo on the error message: "doesnt't"
  @nonExistingIban
  Scenario Outline: Try to create payment with an invalid iban
    When I try to create a payment with the following parameters:
      | debtorIban   | <debtorIban>   |
      | creditorIban | <creditorIban> |
    Then I should receive status code 'BAD_REQUEST' with response contains "<errorMessage>" value

    Examples:
      | debtorIban        | creditorIban      | errorMessage            |
      | TR123456789012345 | NL000000000000001 | Debtor doesnt't exist   |
      | NL000000000000001 | TR123456789012345 | Creditor doesnt't exist |

  # bug - IllegalArgumentException and NullPointerException need to be caught
  # and a proper error code (BAD_REQUEST)/message (XXX must not be null) needs to be returned to the requester.
  @invalidPayload @Skip
  Scenario Outline: Try to create payment without mandatory filed
    When I try to create a new payment without '<field>' parameter
    Then I should receive status code 'BAD_REQUEST' with response contains "<field> must not be null" value

    Examples:
      | field         |
      | debtorIban    |
      | creditorIban  |
      | amount        |
      | transactionId |

  @overTransactionLimit
  Scenario: Validate payment transaction limit
    When I try to create a payment with the following parameters:
      | amount | 1000001 |
    Then I should receive status code 'BAD_REQUEST'
    And the response should contain "Transaction with amount lower than 1000000 can be executed via api" value

  @overBalance
  Scenario: Validate not enough balance
    When I try to create a payment with the following parameters:
      | amount | 987654 |
    Then I should receive status code 'BAD_REQUEST'
    And the response should contain "Debtor doesn't have enough balance" value

  @inactiveAccount
  Scenario Outline: Try to create a new payment from/to inactive account
    When I try to create a payment with the following parameters:
      | debtorIban   | <debtorIban>   |
      | creditorIban | <creditorIban> |
    Then I should receive status code 'BAD_REQUEST' with response contains "<errorMessage>" value

    Examples:
      | debtorIban        | creditorIban      | errorMessage                   |
      | NL000000000000004 | NL000000000000001 | Debtor account is not active   |
      | NL000000000000001 | NL000000000000004 | Creditor account is not active |
