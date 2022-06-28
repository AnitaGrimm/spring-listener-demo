package ru.anitagrimm.springkafkalistenerdemo.kafka.converter;

import java.util.Optional;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class DemoRecordConverter implements RecordConverter<String, String> {

  @Override
  public Optional<Object> convert(ConsumerRecord<String, String> consumerRecord) {
    return Optional.ofNullable(consumerRecord.value());
  }
}
