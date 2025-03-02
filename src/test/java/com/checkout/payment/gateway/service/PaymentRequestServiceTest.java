package com.checkout.payment.gateway.service;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.mapper.PaymentRequestToResponseMapper;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.model.ResponseStatus;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import com.checkout.payment.gateway.validator.PaymentRequestValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.checkout.payment.gateway.constant.PaymentConstants.PAYMENT_AUTHORISED;
import static com.checkout.payment.gateway.constant.PaymentConstants.PAYMENT_DECLINED;
import static com.checkout.payment.gateway.constant.PaymentConstants.PAYMENT_REJECTED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentRequestServiceTest {

  @Mock
  private PaymentsRepository paymentsRepository;

  @Mock
  private PaymentRequestValidator paymentRequestValidator;
  @Mock
  private PaymentRequestToResponseMapper paymentRequestToResponseMapper;

  @Mock
  private AcquiringBankService acquiringBankService;

  @InjectMocks
  private PaymentRequestService paymentRequestService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }
  @Test
  void processPaymentRequestWithValidRequestReturnsAuthorizedStatus() throws JsonProcessingException {
    PostPaymentRequest request = new PostPaymentRequest();
    request.setCardNumber("1234567812345677");
    request.setExpiryMonth(12);
    request.setExpiryYear(2025);
    request.setCurrency("USD");
    request.setCvv(123);

    when(acquiringBankService.postPayment(request)).thenReturn("{\"authorized\":\"true\"}");
    when(paymentRequestToResponseMapper.mapRequestToResponse(any(), any())).thenReturn(new PostPaymentResponse());

    ResponseStatus responseStatus = paymentRequestService.processPaymentRequest(request);

    assertEquals(PaymentStatus.AUTHORIZED, responseStatus.getStatus());
    assertEquals(PAYMENT_AUTHORISED, responseStatus.getMessage());
  }

  @Test
  void processPaymentRequestWithInvalidRequestThrowsException() throws JsonProcessingException {
    PostPaymentRequest request = new PostPaymentRequest();
    request.setCardNumber("123");
    request.setExpiryMonth(12);
    request.setExpiryYear(2025);
    request.setCurrency("USD");
    request.setAmount(1000);
    request.setCvv(123);

    doThrow(new IllegalArgumentException("Invalid card number")).when(paymentRequestValidator).validatePaymentRequest(request);

    ResponseStatus responseStatus = paymentRequestService.processPaymentRequest(request);

    assertEquals(PaymentStatus.REJECTED, responseStatus.getStatus());
    assertEquals(PAYMENT_REJECTED, responseStatus.getMessage());
  }

  @Test
  void processPaymentRequestWithDeclinedResponseReturnsDeclinedStatus() throws JsonProcessingException {
    PostPaymentRequest request = new PostPaymentRequest();
    request.setCardNumber("1234567812345678");
    request.setExpiryMonth(12);
    request.setExpiryYear(2025);
    request.setCurrency("USD");
    request.setAmount(1000);
    request.setCvv(123);

    when(acquiringBankService.postPayment(request)).thenReturn("{\"authorized\":\"false\"}");
    when(paymentRequestToResponseMapper.mapRequestToResponse(any(), any())).thenReturn(new PostPaymentResponse());

    ResponseStatus responseStatus = paymentRequestService.processPaymentRequest(request);

    assertEquals(PaymentStatus.DECLINED, responseStatus.getStatus());
    assertEquals(PAYMENT_DECLINED, responseStatus.getMessage());
  }

  @Test
  void processPaymentRequestWithJsonProcessingExceptionReturnsDeclinedStatus() throws JsonProcessingException {
    PostPaymentRequest request = new PostPaymentRequest();
    request.setCardNumber("1234567812345678");
    request.setExpiryMonth(12);
    request.setExpiryYear(2025);
    request.setCurrency("USD");
    request.setAmount(1000);
    request.setCvv(123);

    when(acquiringBankService.postPayment(request)).thenReturn("invalid json");
    when(paymentRequestToResponseMapper.mapRequestToResponse(any(), any())).thenReturn(new PostPaymentResponse());

    ResponseStatus responseStatus = paymentRequestService.processPaymentRequest(request);

    assertEquals(PaymentStatus.DECLINED, responseStatus.getStatus());
    assertEquals(PAYMENT_DECLINED, responseStatus.getMessage());
  }
}