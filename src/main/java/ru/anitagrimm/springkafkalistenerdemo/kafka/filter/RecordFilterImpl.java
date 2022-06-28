package ru.anitagrimm.springkafkalistenerdemo.kafka.filter;

import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;

@RequiredArgsConstructor
public abstract class RecordFilterImpl<T, K>  implements RecordFilter<T,K>{

  private final Predicate<ConsumerRecord<T,K>> filterPredicate;

  @Override
  public boolean filter(ConsumerRecord<T, K> consumerRecord) {
    return filterPredicate.test(consumerRecord);
  }
}
