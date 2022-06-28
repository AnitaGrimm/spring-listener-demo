package ru.anitagrimm.springkafkalistenerdemo.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Primary
@Component
@ConfigurationProperties(prefix = "demo-kafka-producer")
public class DemoKafkaProducerProperties extends KafkaConnectionProperties {
  private long produceTimeoutMs = 1000;
}
