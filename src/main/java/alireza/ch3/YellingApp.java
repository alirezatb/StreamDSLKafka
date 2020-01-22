package alireza.ch3;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Properties;

public class YellingApp {
    public static void main(String[] args) {
        Properties config = new Properties();
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"alireza:9092");
        config.put(StreamsConfig.APPLICATION_ID_CONFIG,"yelling-app");
        config.put(StreamsConfig.EXACTLY_ONCE, "true");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        StreamsBuilder builder = new StreamsBuilder();
        StreamsConfig streamsConfig = new StreamsConfig(config);

        Serde<String> stringSerde = Serdes.String();
        KStream<String, String> yellingApplication = builder.stream("yelling-app-topic",
                Consumed.with(stringSerde, stringSerde));

        //KStream<String, String> yelling_Uppercase = yellingApplication.mapValues(s -> s.toUpperCase());
        KStream<String, String> yelling_Uppercase = yellingApplication.mapValues(s -> s.toUpperCase());

        yelling_Uppercase.to("yelling-uppercase-app", Produced.with(stringSerde, stringSerde));
        yelling_Uppercase.print(Printed.<String, String>toSysOut().withLabel("yelling-app"));

        KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), config);
        kafkaStreams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));


//        KStream<String, String> simpleFirstStream = builder.stream("src-topic",
//                Consumed.with(stringSerde, stringSerde));
//        KStream<String, String> upperCasedStream = simpleFirstStream.mapValues(String::toUpperCase);
    }
}
