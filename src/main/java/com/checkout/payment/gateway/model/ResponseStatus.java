package com.checkout.payment.gateway.model;

import com.checkout.payment.gateway.enums.PaymentStatus;

public class ResponseStatus {

  public ResponseStatus(PaymentStatus status, String message) {
    this.status = status;
    this.message = message;
  }

  private PaymentStatus status;
  private String message;

  public PaymentStatus getStatus() {
    return status;
  }

  public void setStatus(PaymentStatus status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
