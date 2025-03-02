package com.checkout.payment.gateway.service;

import com.checkout.payment.gateway.exception.EventProcessingException;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.model.ResponseStatus;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for handling payment gateway operations.
 */
@Service
public class PaymentGatewayService {

  private static final Logger LOG = LoggerFactory.getLogger(PaymentGatewayService.class);

  private final PaymentsRepository paymentsRepository;

  @Autowired
  private PaymentRequestService paymentRequestService;

  /**
   * Constructs a PaymentGatewayService with the specified PaymentsRepository.
   *
   * @param paymentsRepository the repository for managing payments
   */
  public PaymentGatewayService(PaymentsRepository paymentsRepository) {
    this.paymentsRepository = paymentsRepository;
  }

  /**
   * Retrieves a payment by its ID.
   *
   * @param id the UUID of the payment
   * @return the PostPaymentResponse containing payment details
   * @throws EventProcessingException if the payment ID is invalid
   */
  public PostPaymentResponse getPaymentById(UUID id) {
    LOG.debug("Requesting access to payment with ID {}", id);
    return paymentsRepository.get(id).orElseThrow(() -> new EventProcessingException("Invalid ID"));
  }

  /**
   * Retrieves all payment event IDs.
   *
   * @return a String containing all payment event IDs
   */
  public String getAllPaymentEventIds() {
    LOG.debug("Requesting all IDs");
    return paymentsRepository.getAllIds();
  }

  /**
   * Processes a payment request.
   *
   * @param paymentRequest the payment request to process
   * @return the ResponseStatus indicating the result of the processing
   * @throws EventProcessingException if there is an error processing the payment request
   */
  public ResponseStatus processPayment(PostPaymentRequest paymentRequest) throws EventProcessingException {
    return paymentRequestService.processPaymentRequest(paymentRequest);
  }
}