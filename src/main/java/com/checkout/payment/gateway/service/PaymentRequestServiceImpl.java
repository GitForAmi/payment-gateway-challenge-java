package com.checkout.payment.gateway.service;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.mapper.PaymentRequestToResponseMapper;
import com.checkout.payment.gateway.model.GatewayResponse;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.model.ResponseStatus;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import com.checkout.payment.gateway.validator.PaymentRequestValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.checkout.payment.gateway.constant.PaymentConstants.AUTHORIZED;
import static com.checkout.payment.gateway.constant.PaymentConstants.DECLINED;
import static com.checkout.payment.gateway.constant.PaymentConstants.PAYMENT_AUTHORISED;
import static com.checkout.payment.gateway.constant.PaymentConstants.PAYMENT_DECLINED;
import static com.checkout.payment.gateway.constant.PaymentConstants.PAYMENT_REJECTED;

/**
 * Service to process payment requests. It validates the request, sends it to the acquiring bank.
 * and adds the response to the repository.
 * The response status is returned to the caller service.
 */
@Service
public class PaymentRequestServiceImpl implements PaymentRequestService{
  private static final Logger LOG = LoggerFactory.getLogger(PaymentRequestService.class);

  private final PaymentsRepository paymentsRepository;
  @Autowired
  private PaymentRequestValidator paymentRequestValidator;
  @Autowired
  private PaymentRequestToResponseMapper paymentRequestToResponseMapper;
  @Autowired
  private AcquiringBankService acquiringBankService;

  @Autowired
  public PaymentRequestServiceImpl(PaymentsRepository paymentsRepository) {
    this.paymentsRepository = paymentsRepository;
  }

  /**
   * Process the payment request by validating the request, sending it to the acquiring bank
   * and adding the response to the repository.
   * @param paymentRequest the payment request
   * @return the response status
   */
  public ResponseStatus processPaymentRequest(PostPaymentRequest paymentRequest) {
    ResponseStatus responseStatus = null;
    try {
      LOG.info("Received payment request: {}", paymentRequest);
      paymentRequestValidator.validatePaymentRequest(paymentRequest);
      PaymentStatus status = extractStatus(acquiringBankService.postPayment(paymentRequest));
      // map payment request to payment response, add UUID and status to the response, keep only last four numbers of the card
      PostPaymentResponse postPaymentResponse = paymentRequestToResponseMapper.mapRequestToResponse(paymentRequest, status);
      LOG.info("Adding payment request to the repository: {}", postPaymentResponse);
      // add the payment request to the repository
      paymentsRepository.add(postPaymentResponse);
      responseStatus = createResponseStatus(status);
    } catch (Exception e) {
      LOG.error("Error processing payment request: {}", e.getMessage());
      responseStatus = createResponseStatus(PaymentStatus.REJECTED);
    }
    return responseStatus;
  }

  /**
   * Extract the payment status from the response. If the response is not in the expected format, return DECLINED.
   * @param response the response from the acquiring bank
   * @return the payment status
   */
  private PaymentStatus extractStatus(String response) {
    GatewayResponse gatewayResponse = null;
    String status = "";
    try {
      gatewayResponse = new ObjectMapper().readValue(response, GatewayResponse.class);
      status = gatewayResponse.getAuthorized().equals("true") ? AUTHORIZED : DECLINED;
    } catch (JsonProcessingException e) {
      status = DECLINED;
    }
    return PaymentStatus.fromString(status);
  }

  /**
   * Create a response status based on the payment status.
   * @param status the payment status
   * @return the response status
   */
  private ResponseStatus createResponseStatus(PaymentStatus status) {
    if(status.equals(PaymentStatus.AUTHORIZED)) {
      return new ResponseStatus(status, PAYMENT_AUTHORISED);
    } else if (status.equals(PaymentStatus.DECLINED)) {
      return new ResponseStatus(status, PAYMENT_DECLINED);
    } else if (status.equals(PaymentStatus.REJECTED)) {
      return new ResponseStatus(status, PAYMENT_REJECTED);
    }
    return null;
  }

}
