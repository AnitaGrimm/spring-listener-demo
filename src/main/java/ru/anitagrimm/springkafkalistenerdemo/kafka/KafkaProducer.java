package ru.anitagrimm.springkafkalistenerdemo.kafka;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
public abstract class KafkaProducer<K, V> {

  public abstract boolean produce(Object data, String id);

  protected Optional<SendResult<K, V>> produce(V data, String topic,
      KafkaTemplate<K, V> template,
      ListenableFutureCallback<SendResult<K, V>> callback, long timeoutMs) {
    try {
      val future = template.send(topic, data);
      future.addCallback(callback);
      return Optional.ofNullable(future.get(timeoutMs, TimeUnit.MILLISECONDS));
    } catch(InterruptedException ie){
      Thread.currentThread().interrupt();
    }
    catch (Exception ex) {
      log.error("{} Exception while waiting callback", topic);
    }
    return Optional.empty();
  }

  protected ListenableFutureCallback<SendResult<K, V>> initCallback(
      Runnable<SendResult<K, V>> onSuccess, Runnable<Throwable> onFailure) {
    return new ListenableFutureCallback<>() {
      @Override
      public void onSuccess(SendResult<K, V> stringObjectSendResult) {
        if (onSuccess != null) {
          onSuccess.run(stringObjectSendResult);
        }
      }
      @Override
      public void onFailure(@NotNull Throwable throwable) {
        if (onFailure != null) {
          onFailure.run(throwable);
        }
      }
    };
  }

  public interface Runnable<T> {

    void run(T data);
  }
}
