package com.checkout.payment.gateway.mapper;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class PaymentRequestToResponseMapperTest {

  @InjectMocks
  private PaymentRequestToResponseMapper mapper;

  @Test
  void mapRequestToResponseWithValidRequestReturnsResponse() {
    PostPaymentRequest request = new PostPaymentRequest();
    request.setCardNumber("1234567812345678");
    request.setExpiryMonth(12);
    request.setExpiryYear(2024);
    request.setCurrency("USD");
    request.setAmount(1000);

    PostPaymentResponse response = mapper.mapRequestToResponse(request, PaymentStatus.AUTHORIZED);

    assertNotNull(response.getId());
    assertEquals("5678", response.getCardNumberLastFour());
    assertEquals(12, response.getExpiryMonth());
    assertEquals(2024, response.getExpiryYear());
    assertEquals("USD", response.getCurrency());
    assertEquals(10.0, response.getAmount());
    assertEquals(PaymentStatus.AUTHORIZED, response.getStatus());
  }

  @Test
  void mapRequestToResponseWithBlankCardNumberReturnsEmptyLastFour() {
    PostPaymentRequest request = new PostPaymentRequest();
    request.setCardNumber("");
    request.setExpiryMonth(12);
    request.setExpiryYear(2024);
    request.setCurrency("USD");
    request.setAmount(1000);

    PostPaymentResponse response = mapper.mapRequestToResponse(request, PaymentStatus.AUTHORIZED);

    assertEquals("", response.getCardNumberLastFour());
  }

  @Test
  void mapRequestToResponseWithNegativeAmountReturnsZeroAmount() {
    PostPaymentRequest request = new PostPaymentRequest();
    request.setCardNumber("1234567812345678");
    request.setExpiryMonth(12);
    request.setExpiryYear(2024);
    request.setCurrency("USD");
    request.setAmount(-1000);

    PostPaymentResponse response = mapper.mapRequestToResponse(request, PaymentStatus.AUTHORIZED);

    assertEquals(0.0, response.getAmount());
  }

  @Test
  void mapRequestToResponseWithZeroAmountReturnsZeroAmount() {
    PostPaymentRequest request = new PostPaymentRequest();
    request.setCardNumber("1234567812345678");
    request.setExpiryMonth(12);
    request.setExpiryYear(2024);
    request.setCurrency("USD");
    request.setAmount(0);

    PostPaymentResponse response = mapper.mapRequestToResponse(request, PaymentStatus.AUTHORIZED);

    assertEquals(0.0, response.getAmount());
  }
}
