package com.checkout.payment.gateway.controller;


import static com.checkout.payment.gateway.constant.PaymentConstants.PAYMENT_REJECTED;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import java.util.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentGatewayControllerTest {

  @Autowired
  private MockMvc mvc;
  @Autowired
  PaymentsRepository paymentsRepository;

  @Test
  void whenPaymentWithIdExistThenCorrectPaymentIsReturned() throws Exception {
    PostPaymentResponse payment = new PostPaymentResponse();
    payment.setId(UUID.randomUUID());
    payment.setAmount(10);
    payment.setCurrency("USD");
    payment.setStatus(PaymentStatus.AUTHORIZED);
    payment.setExpiryMonth(12);
    payment.setExpiryYear(2024);
    payment.setCardNumberLastFour("4321");

    paymentsRepository.add(payment);

    mvc.perform(MockMvcRequestBuilders.get("/payment/" + payment.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(payment.getStatus().getName()))
        .andExpect(jsonPath("$.cardNumberLastFour").value(payment.getCardNumberLastFour()))
        .andExpect(jsonPath("$.expiryMonth").value(payment.getExpiryMonth()))
        .andExpect(jsonPath("$.expiryYear").value(payment.getExpiryYear()))
        .andExpect(jsonPath("$.currency").value(payment.getCurrency()))
        .andExpect(jsonPath("$.amount").value(payment.getAmount()));
  }

  @Test
  void whenPaymentWithIdDoesNotExistThen404IsReturned() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/payment/" + UUID.randomUUID()))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.message").value("Invalid ID"));
  }

  @Test
  void whenGetAllPaymentIdsThenExpectIdReturned() throws Exception {
    PostPaymentResponse payment = new PostPaymentResponse();
    payment.setId(UUID.randomUUID());
    payment.setAmount(10);
    payment.setCurrency("USD");
    payment.setStatus(PaymentStatus.AUTHORIZED);
    payment.setExpiryMonth(12);
    payment.setExpiryYear(2024);
    payment.setCardNumberLastFour("4321");

    paymentsRepository.add(payment);

    mvc.perform(MockMvcRequestBuilders.get("/payment/ids"))
        .andExpect(status().isOk())
//        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$").isString());
  }


  @Test
  void processPaymentWithValidRequestReturnsAuthorized() throws Exception {
    PostPaymentRequest paymentRequest = new PostPaymentRequest();
    paymentRequest.setAmount(10);
    paymentRequest.setCurrency("USD");
    paymentRequest.setCardNumber("1234567812345677");
    paymentRequest.setExpiryMonth(12);
    paymentRequest.setExpiryYear(2025);
    paymentRequest.setCvv(123);

    mvc.perform(MockMvcRequestBuilders.post("/payment/process")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(paymentRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("Authorized"));
  }

  @Test
  void processPaymentWithInValidCardReturnsDeclined() throws Exception {
    PostPaymentRequest paymentRequest = new PostPaymentRequest();
    paymentRequest.setAmount(10);
    paymentRequest.setCurrency("GBP");
    paymentRequest.setCardNumber("1234567812345678");
    paymentRequest.setExpiryMonth(12);
    paymentRequest.setExpiryYear(2025);
    paymentRequest.setCvv(678);

    mvc.perform(MockMvcRequestBuilders.post("/payment/process")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(paymentRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("Declined"));
  }

  @Test
  void processPaymentWithInvalidCurrencyReturnsRejected() throws Exception {
    PostPaymentRequest paymentRequest = new PostPaymentRequest();
    paymentRequest.setAmount(10);
    paymentRequest.setCurrency("INVALID");
    paymentRequest.setCardNumber("1234567812345678");
    paymentRequest.setExpiryMonth(12);
    paymentRequest.setExpiryYear(2025);
    paymentRequest.setCvv(123);

    mvc.perform(MockMvcRequestBuilders.post("/payment/process")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(paymentRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value(PAYMENT_REJECTED));
  }

  @Test
  void processPaymentWithMissingFieldsReturnsRejected() throws Exception {
    PostPaymentRequest paymentRequest = new PostPaymentRequest();
    paymentRequest.setAmount(10);
    paymentRequest.setCurrency("USD");

    mvc.perform(MockMvcRequestBuilders.post("/payment/process")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(paymentRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("Rejected"));
  }

  @Test
  void processPaymentWithExpiredCardReturnsRejected() throws Exception {
    PostPaymentRequest paymentRequest = new PostPaymentRequest();
    paymentRequest.setAmount(10);
    paymentRequest.setCurrency("USD");
    paymentRequest.setCardNumber("1234567812345678");
    paymentRequest.setExpiryMonth(12);
    paymentRequest.setExpiryYear(2020);
    paymentRequest.setCvv(123);

    mvc.perform(MockMvcRequestBuilders.post("/payment/process")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(paymentRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value(PAYMENT_REJECTED));
  }
}
