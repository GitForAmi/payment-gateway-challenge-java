package com.checkout.payment.gateway.model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GatewayResponse {
    private String authorized;
    private String authorization_code;

    public GatewayResponse() {
      super();
    }

  public String getAuthorized() {
    return authorized;
  }

  public void setAuthorized(String authorized) {
    this.authorized = authorized;
  }

  public String getAuthorization_code() {
    return authorization_code;
  }

  public void setAuthorization_code(String authorization_code) {
    this.authorization_code = authorization_code;
  }
}
