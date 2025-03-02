package com.checkout.payment.gateway.validator;

import com.checkout.payment.gateway.model.PostPaymentRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PaymentRequestValidatorTest {

  @InjectMocks
  private PaymentRequestValidator validator;

  @Test
  void validPaymentRequestPassesValidation() {
    PostPaymentRequest request = new PostPaymentRequest();
    request.setCardNumber("1234567812345678");
    request.setExpiryMonth(12);
    request.setExpiryYear(2025);
    request.setCurrency("USD");
    request.setAmount(1000);
    request.setCvv(123);

    assertDoesNotThrow(() -> validator.validatePaymentRequest(request));
  }

  @Test
  void paymentRequestWithInvalidCardNumberThrowsException() {
    PostPaymentRequest request = new PostPaymentRequest();
    request.setCardNumber("123");
    request.setExpiryMonth(12);
    request.setExpiryYear(2025);
    request.setCurrency("USD");
    request.setAmount(1000);
    request.setCvv(123);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validatePaymentRequest(request));
    assertEquals("Card number is required and should be between 14-19 numeric characters long", exception.getMessage());
  }

  @Test
  void paymentRequestWithInvalidExpiryMonthThrowsException() {
    PostPaymentRequest request = new PostPaymentRequest();
    request.setCardNumber("1234567812345678");
    request.setExpiryMonth(13);
    request.setExpiryYear(2025);
    request.setCurrency("USD");
    request.setAmount(1000);
    request.setCvv(123);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validatePaymentRequest(request));
    assertEquals("Expiry month is required and should be between 1-12", exception.getMessage());
  }

  @Test
  void paymentRequestWithPastExpiryYearThrowsException() {
    PostPaymentRequest request = new PostPaymentRequest();
    request.setCardNumber("1234567812345678");
    request.setExpiryMonth(12);
    request.setExpiryYear(2020);
    request.setCurrency("USD");
    request.setAmount(1000);
    request.setCvv(123);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validatePaymentRequest(request));
    assertEquals("Expiry year is required and should be greater than or equal to current year", exception.getMessage());
  }

  @Test
  void paymentRequestWithInvalidCurrencyThrowsException() {
    PostPaymentRequest request = new PostPaymentRequest();
    request.setCardNumber("1234567812345678");
    request.setExpiryMonth(12);
    request.setExpiryYear(2025);
    request.setCurrency("INVALID");
    request.setAmount(1000);
    request.setCvv(123);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validatePaymentRequest(request));
    assertEquals("Currency is required and should be a 3 character long string", exception.getMessage());
  }

  @Test
  void paymentRequestWithNegativeAmountThrowsException() {
    PostPaymentRequest request = new PostPaymentRequest();
    request.setCardNumber("1234567812345678");
    request.setExpiryMonth(12);
    request.setExpiryYear(2025);
    request.setCurrency("USD");
    request.setAmount(-1000);
    request.setCvv(123);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validatePaymentRequest(request));
    assertEquals("Amount is required and should be a positive integer", exception.getMessage());
  }

  @Test
  void paymentRequestWithInvalidCvvThrowsException() {
    PostPaymentRequest request = new PostPaymentRequest();
    request.setCardNumber("1234567812345678");
    request.setExpiryMonth(12);
    request.setExpiryYear(2025);
    request.setCurrency("USD");
    request.setAmount(1000);
    request.setCvv(12);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validatePaymentRequest(request));
    assertEquals("CVV is required and should be a 3-4 digit long integer", exception.getMessage());
  }
}
