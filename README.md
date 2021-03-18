# Payee Payment-API 
## API
API accepts payment transactions ;
```json
 {
     "transactionId" : "transaction-id-123",
     "debtorIban" : "NL000000000000001",
     "creditorIban" : "NL000000000000002",
     "amount" : 1
 }
```
performs validation and sends a booking request to booking-system;
```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<BookingRequest>
    <amount>1</amount>
    <creditorAccountManagementSystem>RAI</creditorAccountManagementSystem>
    <creditorIban>NL000000000000002</creditorIban>
    <debtorAccountManagementSystem>P7</debtorAccountManagementSystem>
    <debtorIban>NL000000000000001</debtorIban>
    <transactionId>transaction-id-123</transactionId>
</BookingRequest>
```
then returns 200 response to the client.

Whenever it receives a bookingResponse from bookingSystem the it make a HTTP Post call to CALLBACk_URL(whichs is set in the environment vars, you can find it in the docker-compose.yml file). Callback;
```json
{
  "transactionId" : "transaction-id-123",
  "status" : "A" /* A: Accepted, R:Rejected*/ 
}
```

## Booking-System
Booking-system is a mock that acks every message(send by API, check the BookingRequest message above) and returns;
```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<BookingResponse>
    <transactionId>transaction-id-123</transactionId>
    <code>0</code>
    <description></description>
</BookingResponse>
```

under _misc/docker/mysql-dump_ folder database initialization script can be found.

You can run whole application with the following components;
- mysql
- activemq
- api
- booking-system
- mock-callback  
by running **docker-compose up** (for shutdown **docker-compose down**)

In order to generate images of api and booking-system you need to run maven spring-boot plugin's build:image task.