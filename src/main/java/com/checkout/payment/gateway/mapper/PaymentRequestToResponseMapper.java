package com.checkout.payment.gateway.mapper;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import java.util.UUID;

/**
 * Mapper class for converting PostPaymentRequest to PostPaymentResponse.
 */
@Component
public class PaymentRequestToResponseMapper {

  /**
   * Maps a PostPaymentRequest to a PostPaymentResponse.
   *
   * @param postPaymentRequest the payment request to map
   * @param status the status of the payment
   * @return the mapped PostPaymentResponse
   */
  public PostPaymentResponse mapRequestToResponse(PostPaymentRequest postPaymentRequest, PaymentStatus status) {
    // generate UUID for the payment request
    // map the payment request to the payment response and add the UUID and status
    PostPaymentResponse response = new PostPaymentResponse();
    response.setId(UUID.randomUUID());
    String cardNumberLastFour = Strings.isNotBlank(postPaymentRequest.getCardNumber()) ? postPaymentRequest.getCardNumber().substring(postPaymentRequest.getCardNumber().length() - 4) : "";
    response.setCardNumberLastFour(cardNumberLastFour);
    response.setExpiryMonth(postPaymentRequest.getExpiryMonth());
    response.setExpiryYear(postPaymentRequest.getExpiryYear());
    response.setCurrency(postPaymentRequest.getCurrency());
    // we may want to use BigDecimal for currency amounts
    Double amount = postPaymentRequest.getAmount() > 0 ? postPaymentRequest.getAmount() / 100.0 : 0.0;
    response.setAmount(amount);
    response.setStatus(status);
    return response;
  }
}
