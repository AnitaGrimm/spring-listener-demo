package ru.anitagrimm.springkafkalistenerdemo.kafka.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.anitagrimm.springkafkalistenerdemo.properties.DemoKafkaListenerProperties;

@Primary
@Component
public class DemoRecordFilter extends RecordValueFilter {

  @Autowired
  public DemoRecordFilter(DemoKafkaListenerProperties properties) {
    super(properties.getFilter().getKeyToMatch(), properties.getFilter().getValueToMatch());
  }
}
