package com.checkout.payment.gateway.validator;

import java.time.Year;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import org.springframework.stereotype.Component;

import static com.checkout.payment.gateway.constant.PaymentConstants.EUR;
import static com.checkout.payment.gateway.constant.PaymentConstants.GBP;
import static com.checkout.payment.gateway.constant.PaymentConstants.USD;

@Component
public class PaymentRequestValidator {

  /**
   * Validates the payment request based on the following rules:
   * 1. Card number is required and should be between 14-19 numeric characters long.
   * 2. Expiry month is required and should be between 1-12.
   * 3. Expiry year is required and should be greater than or equal to the current year.
   * 4. Currency is required and should be a 3 character long string.
   * 5. Amount is required and should be a positive integer.
   * 6. CVV is required and should be a 3-4 digit long integer.
   * @param paymentRequest payment request to be validated
   * @throws IllegalArgumentException if any of the above rules are violated
   */
  public void validatePaymentRequest(PostPaymentRequest paymentRequest) {
    if (paymentRequest.getCardNumber() == null || !paymentRequest.getCardNumber().matches("\\d{14,19}")) {
      throw new IllegalArgumentException("Card number is required and should be between 14-19 numeric characters long");
    }
    if (paymentRequest.getExpiryMonth() < 1 || paymentRequest.getExpiryMonth() > 12) {
      throw new IllegalArgumentException("Expiry month is required and should be between 1-12");
    }
    if (paymentRequest.getExpiryYear() < Year.now().getValue()) {
      throw new IllegalArgumentException("Expiry year is required and should be greater than or equal to current year");
    }

    if (paymentRequest.getCurrency() == null || !paymentRequest.getCurrency().matches("[A-Z]{3}")) {
      throw new IllegalArgumentException("Currency is required and should be a 3 character long string");
    } else {
      if (!USD.equals(paymentRequest.getCurrency()) && !EUR.equals(paymentRequest.getCurrency()) && !GBP.equals(paymentRequest.getCurrency())) {
        throw new IllegalArgumentException("Currency is required and should be either USD, EUR, or GBP");
      }
    }
    // NOTE: Amount should be a positive number
    if (paymentRequest.getAmount() <= 0) {
      throw new IllegalArgumentException("Amount is required and should be a positive integer");
    }
    if (paymentRequest.getCvv() < 100 || paymentRequest.getCvv() > 9999) {
      throw new IllegalArgumentException("CVV is required and should be a 3-4 digit long integer");
    }
  }
}