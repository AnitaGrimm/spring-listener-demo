package ru.anitagrimm.springkafkalistenerdemo.properties;

import lombok.Getter;
import lombok.Setter;
import ru.anitagrimm.springkafkalistenerdemo.util.KafkaUtils;

@Getter
@Setter
public abstract class KafkaConnectionProperties {
  private String topicName;
  private String processName;
  private String clientIdPrefix;
  private String clientIdPostfix;
  private String bootstrapServers;

  public String getClientId(){
    return KafkaUtils.generateUniqueClientId(clientIdPrefix, processName, clientIdPostfix);
  }
}
