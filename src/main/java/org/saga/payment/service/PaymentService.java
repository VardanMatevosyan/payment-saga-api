package org.saga.payment.service;


import org.saga.common.dto.inventory.event.InventoryPaymentEvent;
import org.saga.common.enums.PaymentStatus;
import org.springframework.retry.annotation.Retryable;

public interface PaymentService {

  @Retryable(maxAttempts = 3, retryFor = RuntimeException.class)
  void mockCreditMoney(String userEmail, Integer price);

  @Retryable(maxAttempts = 3, retryFor = Exception.class)
  void createTransaction(InventoryPaymentEvent eventDto);

  void updatePaymentStatus(Long orderId, PaymentStatus paymentStatus);
}
