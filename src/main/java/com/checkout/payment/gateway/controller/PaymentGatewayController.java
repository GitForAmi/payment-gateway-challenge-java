package com.checkout.payment.gateway.controller;

import com.checkout.payment.gateway.exception.EventProcessingException;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.model.ResponseStatus;
import com.checkout.payment.gateway.service.PaymentGatewayService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("api")
public class PaymentGatewayController {

  @Autowired
  private PaymentGatewayService paymentGatewayService;

  @GetMapping("/payment/{id}")
  public ResponseEntity<PostPaymentResponse> getPostPaymentEventById(@PathVariable UUID id) {
    return new ResponseEntity<>(paymentGatewayService.getPaymentById(id), HttpStatus.OK);
  }

  @GetMapping("/payment/ids")
  public ResponseEntity<String> getAllEventIds() {
    return new ResponseEntity<>(paymentGatewayService.getAllPaymentEventIds(), HttpStatus.OK);
  }

  @PostMapping("/payment/process")
  public ResponseEntity<ResponseStatus> processPayment(@RequestBody PostPaymentRequest paymentRequest) throws EventProcessingException {
    return new ResponseEntity<>(paymentGatewayService.processPayment(paymentRequest), HttpStatus.OK);
  }
}
