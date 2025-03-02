package com.checkout.payment.gateway.constant;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PaymentConstantsTest {

  @Test
  void paymentAuthorisedConstantIsCorrect() {
    assertEquals("the payment was authorized by the call to the acquiring bank", PaymentConstants.PAYMENT_AUTHORISED);
  }

  @Test
  void paymentDeclinedConstantIsCorrect() {
    assertEquals("the payment was declined by the call to the acquiring bank", PaymentConstants.PAYMENT_DECLINED);
  }

  @Test
  void paymentRejectedConstantIsCorrect() {
    assertEquals("No payment could be created as invalid information was supplied to the payment gateway and therefore it has rejected the request without calling the acquiring bank", PaymentConstants.PAYMENT_REJECTED);
  }

  @Test
  void usdConstantIsCorrect() {
    assertEquals("USD", PaymentConstants.USD);
  }

  @Test
  void gbpConstantIsCorrect() {
    assertEquals("GBP", PaymentConstants.GBP);
  }

  @Test
  void eurConstantIsCorrect() {
    assertEquals("EUR", PaymentConstants.EUR);
  }
}