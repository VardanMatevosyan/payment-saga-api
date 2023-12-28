package org.saga.payment.service.impl;


import jakarta.transaction.Transactional;
import java.util.Random;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.saga.common.dto.inventory.event.InventoryPaymentEvent;
import org.saga.common.enums.PaymentStatus;
import org.saga.payment.entity.TransactionLog;
import org.saga.payment.mapper.TransactionLogMapper;
import org.saga.payment.repository.TransactionLogRepository;
import org.saga.payment.service.PaymentService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentServiceImpl implements PaymentService {

  TransactionLogRepository transactionLogRepository;
  TransactionLogMapper transactionLogMapper;

  @Override
  public void mockCreditMoney(String userEmail, Integer price) {
    int randomInt = new Random().nextInt(5);
    if (randomInt > 2) {
      log.info("credit is successful for the user %s".formatted(userEmail));
    } else {
      String failedMessage = "credit is failed for the user %s".formatted(userEmail);
      log.info(failedMessage);
      throw new RuntimeException(failedMessage);
    }
  }

  @Override
  public void createTransaction(InventoryPaymentEvent eventDto) {
    TransactionLog transactionLog = transactionLogMapper.toEntity(eventDto);
    Long orderId = eventDto.getInventoryDto().getOrderId();
    var existsTransactionLog = transactionLogRepository.findTransactionLogByOrderId(orderId);
    if (existsTransactionLog.isEmpty()) {
      transactionLogRepository.save(transactionLog);
    }
  }

  @Override
  @Transactional
  public void updatePaymentStatus(Long orderId, PaymentStatus paymentStatus) {
    transactionLogRepository
        .findTransactionLogByOrderId(orderId)
        .ifPresent(tl -> tl.setPaymentStatus(paymentStatus));
  }
}
