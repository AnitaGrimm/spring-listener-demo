package ru.anitagrimm.springkafkalistenerdemo.context;


import static ru.anitagrimm.springkafkalistenerdemo.util.KafkaUtils.generateUniqueClientId;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import ru.anitagrimm.springkafkalistenerdemo.properties.DemoKafkaListenerProperties;
import ru.anitagrimm.springkafkalistenerdemo.properties.KafkaCommonProperties;
import ru.anitagrimm.springkafkalistenerdemo.util.KafkaUtils;

@Configuration
public class MainContext {

  @Bean
  public ConsumerFactory<String, String> demoConsumerFactory(
      KafkaCommonProperties kafkaProperties,
      DemoKafkaListenerProperties properties) {
    return new DefaultKafkaConsumerFactory<>(
        KafkaUtils.getConsumerProperties(properties.getGroupId(),
            generateUniqueClientId(properties.getClientIdPrefix(),
                properties.getProcessName(),
                properties.getClientIdPostfix()),
            properties.getBootstrapServers(), StringDeserializer.class, kafkaProperties));
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, String> demoContainerFactory(
      ConsumerFactory<String, String> demoConsumerFactory,
      KafkaCommonProperties kafkaProperties,
      DemoKafkaListenerProperties demoKafkaListenerProperties) {
    return KafkaUtils.getConcurrentKafkaListenerContainerFactory(
        demoConsumerFactory,
        kafkaProperties,
        demoKafkaListenerProperties.getConcurrency(),
        demoKafkaListenerProperties.getCommitLogLevel());
  }
}
