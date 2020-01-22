package alireza.ch2.consumer;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.StreamsConfig;

import java.util.Properties;

public class TransactionalConsumerMainThread {
    public static void main(String[] args) {
        Properties config = new Properties();
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "alireza:9092");
        config.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaAvroDeserializer");
        config.put("specific.avro.reader", "true"); // avro specific record reader activated
        config.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
        config.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "60000");


        // transaction properties.
        config.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // group id. MAke it Dynamic

        config.put(ConsumerConfig.GROUP_ID_CONFIG, "groupId");

        TransactionalConsumer tConsumer= new TransactionalConsumer(config, "transaction topic");
        Thread consumerThread = new Thread(tConsumer);
        consumerThread.start();

        Thread mainThread = Thread.currentThread();
        // register Message as shutdown hook
        Runtime.getRuntime().addShutdownHook(new ShutdownHook<>(tConsumer, mainThread));

    }
}
