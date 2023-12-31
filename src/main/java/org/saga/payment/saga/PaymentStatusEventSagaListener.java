package org.saga.payment.saga;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.saga.common.dto.payment.event.PaymentStatusEvent;
import org.saga.common.enums.PaymentStatus;
import org.saga.payment.service.PaymentService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentStatusEventSagaListener {

  final PaymentService paymentService;

  @RetryableTopic(attempts = "3")
  @KafkaListener(groupId = "payment-consumer", topics = {"payment-api-payment-status-topic"})
  public void onOrderCreate(PaymentStatusEvent paymentStatusEvent) {
    log.info("Received payment status event %s".formatted(paymentStatusEvent));
    PaymentStatus paymentStatus = paymentStatusEvent.getPaymentStatus();
    Long orderId = paymentStatusEvent.getOrderId();
    paymentService.updatePaymentStatus(orderId, paymentStatus);
  }

}
