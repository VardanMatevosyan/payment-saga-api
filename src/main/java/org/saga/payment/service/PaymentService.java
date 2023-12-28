package org.saga.payment.service;


import org.saga.common.dto.inventory.event.InventoryPaymentEvent;
import org.saga.common.enums.PaymentStatus;

public interface PaymentService {

  void mockCreditMoney(String userEmail, Integer price);

  void createTransaction(InventoryPaymentEvent eventDto);

  void updatePaymentStatus(Long orderId, PaymentStatus paymentStatus);
}
