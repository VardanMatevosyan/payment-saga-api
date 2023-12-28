package org.saga.payment.mapper;

import java.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.saga.common.dto.inventory.event.InventoryPaymentEvent;
import org.saga.common.enums.PaymentStatus;
import org.saga.payment.entity.PaymentMethod;
import org.saga.payment.entity.TransactionLog;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    imports = {LocalDateTime.class, PaymentMethod.class, PaymentStatus.class}
)
public interface TransactionLogMapper {

  @Mapping(target = "orderId", source = "eventDto.inventoryDto.orderId")
  @Mapping(target = "eventId", expression = "java(eventDto.getEventId().toString())")
  @Mapping(target = "customerEmail", source = "eventDto.inventoryDto.customerEmail")
  @Mapping(target = "price", source = "eventDto.inventoryDto.orderPrice")
  @Mapping(target = "paymentDate", expression = "java(LocalDateTime.now())")
  @Mapping(target = "paymentMethod", expression = "java(PaymentMethod.VISA)")
  @Mapping(target = "paymentStatus", expression = "java(PaymentStatus.PAYMENT_PENDING)")
  TransactionLog toEntity(InventoryPaymentEvent eventDto);
}
