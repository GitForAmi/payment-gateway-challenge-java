package com.checkout.payment.gateway.repository;

import com.checkout.payment.gateway.model.PostPaymentResponse;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/**
 * Repository class for managing payments.
 */
@Repository
public class PaymentsRepository {

  private final HashMap<UUID, PostPaymentResponse> payments = new HashMap<>();

  /**
   * Adds a payment to the repository.
   * @param payment the payment to add
   */
  public void add(PostPaymentResponse payment) {
    payments.put(payment.getId(), payment);
  }

  /**
   * Retrieves a payment by its ID.
   * @param id the UUID of the payment
   * @return the PostPaymentResponse containing payment details
   */
  public Optional<PostPaymentResponse> get(UUID id) {
    return Optional.ofNullable(payments.get(id));
  }

  /**
   * Returns all payment IDs
   * @return all payment IDs
   */
  public String getAllIds() {
    return String.join(",", payments.keySet().stream().map(UUID::toString).toArray(String[]::new));
  }
}
