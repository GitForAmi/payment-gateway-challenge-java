package com.checkout.payment.gateway.service;

import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static com.checkout.payment.gateway.constant.PaymentConstants.AUTH_GATEWAY_URL;

/**
 * Service to send payment requests to the acquiring bank.
 */
@Service
public class AcquiringBankService {
  private static final Logger LOG = LoggerFactory.getLogger(AcquiringBankService.class);
  @Autowired
  private final RestTemplate restTemplate;

  @Value("${auth.gateway.url}")
  private String authGatewayUrl;

  public AcquiringBankService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  /**
   * The payment request is converted to JSON and Sent to the acquiring bank.
   * @param paymentRequest the payment request
   * @return the response from the acquiring bank
   * @throws JsonProcessingException if the payment request cannot be converted to JSON
   */
  public String postPayment(PostPaymentRequest paymentRequest) throws JsonProcessingException {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    // convert payment request to JSON
    String jsonPayload = new ObjectMapper().writeValueAsString(paymentRequest);
    LOG.info("Sending payment request to acquiring bank: {}", jsonPayload);
    HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

    try {
      ResponseEntity<String> gatewayResponse = restTemplate.exchange(authGatewayUrl, HttpMethod.POST, request, String.class);
      LOG.info("Received response status {} with body {} from acquiring bank", gatewayResponse.getStatusCode(), gatewayResponse.getBody());
      return gatewayResponse.getBody();
    } catch (HttpServerErrorException.ServiceUnavailable e) {
      LOG.error("Service unavailable: {}", e.getMessage());
      return ""; // rethrow the exception or handle it as needed
    }
  }
}
