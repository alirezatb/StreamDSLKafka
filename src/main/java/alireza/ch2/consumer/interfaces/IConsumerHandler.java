package alireza.ch2.consumer.interfaces;

import org.apache.kafka.clients.consumer.KafkaConsumer;

public interface ConsumerHandler<K, V> extends Runnable {
    KafkaConsumer<K,V> getConsumer();
    void setWakeupCalled(boolean WakedupCalled);
}
