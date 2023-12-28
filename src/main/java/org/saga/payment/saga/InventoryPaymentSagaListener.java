package org.saga.payment.saga;



import static org.saga.common.enums.PaymentStatus.PAYMENT_COMPLETED;
import static org.saga.common.enums.PaymentStatus.PAYMENT_REJECTED;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.saga.common.dto.EventDto;
import org.saga.common.dto.inventory.InventoryDto;
import org.saga.common.dto.inventory.event.InventoryPaymentEvent;
import org.saga.common.enums.PaymentStatus;
import org.saga.payment.mapper.PaymentEventDtoMapper;
import org.saga.payment.service.PaymentService;
import org.saga.payment.service.impl.MessageBroker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryPaymentSagaListener {

  final MessageBroker messageBroker;
  final PaymentService paymentService;
  final PaymentEventDtoMapper paymentEventDtoMapper;

  @Value("${payment-saga-topic}")
  String paymentSagaTopic;
  @Value("${inventory-payment-saga-topic-dlt}")
  String inventoryPaymentSagaTopicDlt;


  @KafkaListener(groupId = "payment-consumer", topics = {"inventory-payment-saga-topic"})
  public void onOrderCreate(InventoryPaymentEvent inventoryPaymentEvent) {
    log.info("Received inventory payment event %s".formatted(inventoryPaymentEvent));
    InventoryDto inventoryDto = inventoryPaymentEvent.getInventoryDto();
    // todo need to retry each separately
    try {
      paymentService.createTransaction(inventoryPaymentEvent);
      paymentService.mockCreditMoney(inventoryDto.getCustomerEmail(), inventoryDto.getOrderPrice());
      paymentService.updatePaymentStatus(inventoryDto.getOrderId(), PAYMENT_COMPLETED);
      sendPaymentEvent(inventoryDto, PAYMENT_COMPLETED);
    } catch (RuntimeException e) {
      log.info("Can't process inventory payment message. Sending message to %s topic"
          .formatted(inventoryPaymentSagaTopicDlt));
      messageBroker.send(inventoryPaymentSagaTopicDlt, inventoryPaymentEvent);
    }
  }

  private void sendPaymentEvent(InventoryDto inventoryDto, PaymentStatus paymentStatus) {
    EventDto eventDto = paymentEventDtoMapper.toDto(inventoryDto, paymentStatus);
    messageBroker.send(paymentSagaTopic, eventDto);
  }

  @KafkaListener(groupId = "payment-consumer", topics = {"inventory-payment-saga-topic-dlt"})
  public void processOrderCreateEventAfterRetries(InventoryPaymentEvent inventoryPaymentEvent) {
    log.info("Handling DLT message from %s".formatted(inventoryPaymentSagaTopicDlt));
    Long orderId = inventoryPaymentEvent.getInventoryDto().getOrderId();
    paymentService.updatePaymentStatus(orderId, PAYMENT_REJECTED);
    sendPaymentEvent(inventoryPaymentEvent.getInventoryDto(), PaymentStatus.PAYMENT_REJECTED);
  }


}
