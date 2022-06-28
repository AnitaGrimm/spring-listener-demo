package ru.anitagrimm.springkafkalistenerdemo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.anitagrimm.springkafkalistenerdemo.kafka.converter.DemoRecordConverter;
import ru.anitagrimm.springkafkalistenerdemo.kafka.filter.DemoRecordFilter;

@Slf4j
@Component
@EnableKafka
@RequiredArgsConstructor
public class DemoKafkaListener {

  private final DemoKafkaProducer producer;
  private final DemoRecordFilter filter;
  private final DemoRecordConverter converter;

  @KafkaListener(topics = "${demo-kafka-listener.topic-name}", containerFactory = "demoContainerFactory")
  public void listen(ConsumerRecord<String, String> record) {
    if (filter.filter(record)) {
      Object afterChanges = converter.convert(record);
      producer.produce(afterChanges, record.key());
    }
  }
}
