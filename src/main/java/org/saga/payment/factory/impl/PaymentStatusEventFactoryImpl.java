package org.saga.payment.factory.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.saga.common.dto.payment.event.PaymentStatusEvent;
import org.saga.common.enums.PaymentStatus;
import org.saga.payment.factory.PaymentStatusEventFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentStatusEventFactoryImpl implements PaymentStatusEventFactory {

  @Override
  public PaymentStatusEvent build(PaymentStatus paymentStatus) {
    return PaymentStatusEvent.builder()
        .paymentStatus(paymentStatus)
        .build();
  }

}
