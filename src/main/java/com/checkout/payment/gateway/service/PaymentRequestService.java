package com.checkout.payment.gateway.service;

import static com.checkout.payment.gateway.constant.PaymentConstants.AUTHORIZED;
import static com.checkout.payment.gateway.constant.PaymentConstants.DECLINED;
import static com.checkout.payment.gateway.constant.PaymentConstants.PAYMENT_AUTHORISED;
import static com.checkout.payment.gateway.constant.PaymentConstants.PAYMENT_DECLINED;
import static com.checkout.payment.gateway.constant.PaymentConstants.PAYMENT_REJECTED;

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

public interface PaymentRequestService {

  ResponseStatus processPaymentRequest(PostPaymentRequest paymentRequest);
}
