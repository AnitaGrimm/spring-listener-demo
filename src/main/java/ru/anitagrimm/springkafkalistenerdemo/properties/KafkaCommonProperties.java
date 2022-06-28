package ru.anitagrimm.springkafkalistenerdemo.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "kafka")
public class KafkaCommonProperties {

  private String trustStoreLocation;
  private String keyStoreLocation;
  private String trustStorePassword;
  private String keyStorePassword;
  private long produceTimeoutMs = 5000;
  private int pollingDurationMs = 5000;
  private int commitDurationMs = 5000;
  private int commitRetriesCount = 3;
  private int maxPollRecordsCount = 500;
  private int maxPollIntervalMs = 360000;
  private int logInitDelayMs = 60000;
  private int logDelayMs = 30000;
  private int logPoolSize = 1;
}
