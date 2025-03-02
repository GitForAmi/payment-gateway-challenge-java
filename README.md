# payment-gateway-challenge-java

### Project Overview

The `payment-gateway-challenge-java` project is a Spring Boot application designed to handle payment gateway operations. It includes various components such as services, repositories, mappers, and configuration classes to manage and process payment requests.

### Configuration

#### `ApplicationConfiguration`
This class is responsible for defining Spring Beans and configuring the application. It includes a `RestTemplate` bean used for making HTTP requests.

### Repository

#### `PaymentsRepository`
This class manages the storage and retrieval of payment data. It uses a `HashMap` to store `PostPaymentResponse` objects.

### Mappers

#### `PaymentRequestToResponseMapper`
This class maps `PostPaymentRequest` objects to `PostPaymentResponse` objects.

### Services

#### `AcquiringBankService`
This service handles communication with the acquiring bank. It uses a `RestTemplate` to send payment requests to Acquiring Bank Service and receive responses.

#### `PaymentGatewayService`
This service handles the core payment gateway operations, including retrieving and processing payments.

#### `PaymentRequestService`
This service processes payment requests by validating them, sending them to the acquiring bank, and storing the responses.

### Test Classes and Coverage

The project includes test classes to ensure the functionality of the services and components. These tests cover various scenarios, including successful payment processing, validation errors, and communication with the acquiring bank.

- **Unit Tests**: Test individual components like `PaymentRequestService`, `PaymentRequestValidator`, and `PaymentGatewayService`.
- **Integration Tests**: Test the interaction between components and the overall workflow of the payment processing ensuring main business scenarios are covered.

The test coverage (94%) ensures that all critical paths and edge cases are tested, providing confidence in the reliability and correctness of the application.

