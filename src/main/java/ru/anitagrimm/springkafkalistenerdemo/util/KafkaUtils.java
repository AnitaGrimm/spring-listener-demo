package ru.anitagrimm.springkafkalistenerdemo.util;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.support.LogIfLevelEnabled.Level;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.anitagrimm.springkafkalistenerdemo.properties.KafkaCommonProperties;

@Slf4j
@RequiredArgsConstructor
public class KafkaUtils {

  public static String generateUniqueClientId(String clientIdPrefix, String processName,
      String clientIdPostfix) {
    return String.format("%s-%s-%s-%s", clientIdPrefix, processName, UUID.randomUUID(), clientIdPostfix);
  }

  public static <T,C> KafkaTemplate<String, T> getProducerTemplate(String clientId,
      String bootstrapServers, Class<C> serializerClass, KafkaCommonProperties kafkaProperties) {
    return new KafkaTemplate<>(
        getProducerFactory(clientId, bootstrapServers, serializerClass, kafkaProperties));
  }

  public static <T,C> ProducerFactory<String, T> getProducerFactory(String clientId,
      String bootstrapServers, Class<C> serializerClass, KafkaCommonProperties kafkaProperties) {
    return new DefaultKafkaProducerFactory<>(
        getProducerProperties(clientId, bootstrapServers, serializerClass, kafkaProperties)
    );
  }

  public static <T> Map<String, Object> getProducerProperties(String clientId, String bootstrapServers,
      Class<T> serializerClass, KafkaCommonProperties kafkaProperties) {
    Map<String, Object> producerProperties = new HashMap<>();
    producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    producerProperties.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
    producerProperties.put(ProducerConfig.RETRIES_CONFIG, 5);
    producerProperties.put(ProducerConfig.ACKS_CONFIG, "all");
    producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, serializerClass);
    return getAddSslProperties(producerProperties, kafkaProperties);
  }

  public static <T> Map<String, Object> getConsumerProperties(String groupId, String clientId,
      String bootstrapServers, Class<T> deserializerClass, KafkaCommonProperties kafkaProperties) {
    Map<String, Object> consumerProperties = new HashMap<>();
    consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    consumerProperties.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
    consumerProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
    consumerProperties
        .put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaProperties.getMaxPollRecordsCount());
    consumerProperties
        .put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, kafkaProperties.getMaxPollIntervalMs());
    consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    consumerProperties
        .put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializerClass);
    consumerProperties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
    return getAddSslProperties(consumerProperties, kafkaProperties);
  }

  private static Map<String, Object> getAddSslProperties(Map<String, Object> properties,
      KafkaCommonProperties kafkaProperties) {
    properties.put("security.protocol", "SSL");
    properties.put("ssl.protocol", "SSL");
    properties.put("ssl.keystore.type", "PKCS12");
    properties.put("ssl.keystore.location", kafkaProperties.getKeyStoreLocation());
    properties.put("ssl.keystore.password", kafkaProperties.getKeyStorePassword());
    properties.put("ssl.truststore.type", "PKCS12");
    properties.put("ssl.truststore.location", kafkaProperties.getTrustStoreLocation());
    properties.put("ssl.truststore.password", kafkaProperties.getTrustStorePassword());
    properties.put("ssl.endpoint.identification.algorithm", "");
    return properties;
  }

  public static <T, K> ConcurrentKafkaListenerContainerFactory<T, K> getConcurrentKafkaListenerContainerFactory(
      ConsumerFactory<T, K> consumerFactory,
      KafkaCommonProperties kafkaProperties,
      Integer concurrency,
      Level commitLogLevel) {
    ConcurrentKafkaListenerContainerFactory<T, K> containerFactory =
        new ConcurrentKafkaListenerContainerFactory<>();
    containerFactory.setConsumerFactory(consumerFactory);
    containerFactory.setConcurrency(concurrency);
    containerFactory.getContainerProperties()
        .setPollTimeout(kafkaProperties.getPollingDurationMs());
    containerFactory.getContainerProperties().setCommitLogLevel(commitLogLevel);
    containerFactory.getContainerProperties().setSyncCommitTimeout(
        Duration.ofMillis(kafkaProperties.getCommitRetriesCount()));
    containerFactory.getContainerProperties().setCommitRetries(
        kafkaProperties.getCommitRetriesCount());
    containerFactory.getContainerProperties().setAckMode(AckMode.BATCH);
    containerFactory.setBatchListener(true);
    return containerFactory;
  }
}
