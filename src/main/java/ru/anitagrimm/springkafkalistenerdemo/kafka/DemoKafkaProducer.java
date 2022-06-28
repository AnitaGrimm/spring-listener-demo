package ru.anitagrimm.springkafkalistenerdemo.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;
import ru.anitagrimm.springkafkalistenerdemo.properties.DemoKafkaProducerProperties;
import ru.anitagrimm.springkafkalistenerdemo.properties.KafkaCommonProperties;
import ru.anitagrimm.springkafkalistenerdemo.util.KafkaUtils;

@Slf4j
@Service
public class DemoKafkaProducer extends KafkaProducer<String, Object> {

  private final KafkaTemplate<String, Object> template;
  private final DemoKafkaProducerProperties properties;

  public DemoKafkaProducer(KafkaCommonProperties kafkaCommonProperties,
      DemoKafkaProducerProperties producerProperties) {
    this.properties = producerProperties;
    this.template = KafkaUtils.getProducerTemplate(producerProperties.getClientId(),
        producerProperties.getBootstrapServers(), JsonSerializer.class, kafkaCommonProperties);
  }

  @Override
  public boolean produce(Object data, String id) {
    return super.produce(data, properties.getTopicName(), template, super.initCallback(
        successCallback -> log.info("{} {}: Data put in kafka successfully", properties.getTopicName(), id),
        failureCallback -> log.error("{} {}: Couldn't put data in kafka", properties.getTopicName(), id)),
        properties.getProduceTimeoutMs()).isPresent();
  }
}
