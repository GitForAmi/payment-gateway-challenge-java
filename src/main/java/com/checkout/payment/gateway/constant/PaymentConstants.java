package com.checkout.payment.gateway.constant;

public class PaymentConstants {

  public static final String PAYMENT_AUTHORISED = "the payment was authorized by the call to the acquiring bank";
  public static final String PAYMENT_DECLINED = "the payment was declined by the call to the acquiring bank";
  public static final String PAYMENT_REJECTED = "No payment could be created as invalid information was supplied to the payment gateway and therefore it has rejected the request without calling the acquiring bank";
  public static final String USD = "USD";
  public static final String GBP = "GBP";
  public static final String EUR = "EUR";
  public static final String AUTHORIZED = "Authorized";
  public static final String DECLINED = "Declined";
  public static final String REJECTED = "Rejected";
  public static final String AUTH_GATEWAY_URL = "http://localhost:8080/payments";


}
