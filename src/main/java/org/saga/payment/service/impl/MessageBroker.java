package org.saga.payment.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.saga.common.dto.EventDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageBroker {

  KafkaTemplate<String, Object> kafkaTemplate;

  public void send(String topicName, EventDto eventDto) {
    try {
      log.info("Trying to send event %s to %s ".formatted(eventDto, topicName));
      kafkaTemplate.send(topicName, eventDto);
    } catch (RuntimeException e) {
      log.info("Can't send event %s to the topic %s".formatted(eventDto, topicName));
      throw e;
    }
  }

}
