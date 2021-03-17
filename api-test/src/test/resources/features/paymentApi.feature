@paymentApi
Feature: Payment API

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

  @overTransactionLimit
  Scenario: Validate payment transaction limit
    When I try to create a payment with the following parameters:
      | amount | 987654 |
    Then I should receive status code 'BAD_REQUEST'
    And the response should contain "Debtor doesn't have enough balance" value
