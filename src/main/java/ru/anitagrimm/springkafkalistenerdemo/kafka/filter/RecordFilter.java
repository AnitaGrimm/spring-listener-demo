package ru.anitagrimm.springkafkalistenerdemo.kafka.filter;

import org.apache.kafka.clients.consumer.ConsumerRecord;

@FunctionalInterface
public interface RecordFilter<T, K> {

  boolean filter(ConsumerRecord<T, K> consumerRecord);
}
