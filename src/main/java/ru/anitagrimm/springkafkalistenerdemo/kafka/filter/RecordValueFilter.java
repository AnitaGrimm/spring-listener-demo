package ru.anitagrimm.springkafkalistenerdemo.kafka.filter;

import com.jayway.jsonpath.JsonPath;
import java.util.Objects;
import java.util.function.Function;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.util.Strings;

public abstract class RecordValueFilter extends RecordFilterImpl<String, String> {

  public RecordValueFilter(String jsonKey, String jsonValue){
    this(consumerRecord ->{
      try {
        return JsonPath.read(consumerRecord.value(), jsonKey).toString();
      } catch (Exception ex) {
        return null;
      }
    }, jsonValue);
  }

  public RecordValueFilter(
      Function<ConsumerRecord<String, String>, String> valueExtractor,
      String valueToMatch) {
    super(kafkaRecord -> {
      if (kafkaRecord != null && !Strings.isEmpty(kafkaRecord.toString())) {
        return Objects.equals(valueToMatch, valueExtractor.apply(kafkaRecord));
      }
      return false;
    });
  }
}
