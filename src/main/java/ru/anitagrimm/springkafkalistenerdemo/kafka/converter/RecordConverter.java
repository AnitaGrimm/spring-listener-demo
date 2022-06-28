package ru.anitagrimm.springkafkalistenerdemo.kafka.converter;

import java.util.Optional;
import org.apache.kafka.clients.consumer.ConsumerRecord;

@FunctionalInterface
public interface RecordConverter<T, K> {

  Optional<Object> convert(ConsumerRecord<T, K> consumerRecord);
}
