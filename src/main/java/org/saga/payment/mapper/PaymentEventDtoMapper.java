package org.saga.payment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.saga.common.dto.inventory.InventoryDto;
import org.saga.common.dto.payment.event.PaymentEvent;
import org.saga.common.enums.PaymentStatus;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentEventDtoMapper {


  @Mapping(target = "paymentDto", source = "inventoryDto")
  @Mapping(target = "paymentStatus", source = "paymentStatus")
  PaymentEvent toDto(InventoryDto inventoryDto, PaymentStatus paymentStatus);

}
