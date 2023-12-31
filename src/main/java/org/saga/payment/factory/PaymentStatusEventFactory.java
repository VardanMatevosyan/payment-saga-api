package org.saga.payment.factory;

import org.saga.common.dto.payment.event.PaymentStatusEvent;
import org.saga.common.enums.PaymentStatus;

public interface PaymentStatusEventFactory {

  PaymentStatusEvent build(PaymentStatus paymentStatus);

}
