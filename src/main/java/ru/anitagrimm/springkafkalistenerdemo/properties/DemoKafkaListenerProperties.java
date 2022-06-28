package ru.anitagrimm.springkafkalistenerdemo.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.support.LogIfLevelEnabled.Level;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@Primary
@Component
@ConfigurationProperties(prefix = "demo-kafka-listener")
public class DemoKafkaListenerProperties extends KafkaConnectionProperties {
  private int concurrency = 1;
  private Level commitLogLevel;
  private String groupId;
  private FilterProperties filter;

  @Getter
  @Setter
  public static class FilterProperties{
    private String keyToMatch;
    private String valueToMatch;
  }
}
