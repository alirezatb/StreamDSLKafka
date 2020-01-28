package alireza.ch3.zmarket;

import alireza.model.Purchase;
import alireza.model.PurchasePattern;
import alireza.model.RewardAccumulator;
import alireza.serializer.JsonDeserializer;
import alireza.serializer.JsonSerializer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;

import java.util.HashMap;
import java.util.Map;

public class ZMartStreamApp {

    public static void main(String[] args) {
        StreamsConfig streamsConfig= new StreamsConfig(properties);
        StreamsBuilder streamsBuilder=new StreamsBuilder();
        Map<String, Object> serdeProps = new HashMap<>();
        final Serializer<Purchase> purchaseJsonSerializer= new JsonSerializer<>();
        serdeProps.put("JsonClass", Purchase.class);
        purchaseJsonSerializer.configure(serdeProps,false);
        final Deserializer<Purchase> purchaseJsonDeserializer = new JsonDeserializer<>();
        serdeProps.put("JsonClass", Purchase.class);
        purchaseJsonDeserializer.configure(serdeProps,false);

        Serde<String> stringSerde = Serdes.String();

        final Serde<Purchase> purchaseSerde =
                Serdes.serdeFrom(purchaseJsonSerializer, purchaseJsonDeserializer);

        KStream<String, Purchase> purchaseKStream = streamsBuilder.stream("transaction",
                Consumed.with(Serdes.String(), purchaseSerde))
                .mapValues(p -> Purchase.builder(p).maskCreditCard().build());
        purchaseKStream.print(Printed.<String, Purchase>toSysOut().withLabel("Purchase Transactions"));
        purchaseKStream.to("purchase", Produced.with(stringSerde, purchaseSerde));

        final Serializer<PurchasePattern> purchasePatternSerializer= new JsonSerializer<>();
        serdeProps.put("JsonClass", PurchasePattern.class);
        purchasePatternSerializer.configure(serdeProps, false);
        final Deserializer<PurchasePattern> purchasePatternDeserializer= new JsonDeserializer<>();
        serdeProps.put("JsonClass", PurchasePattern.class);
        purchasePatternDeserializer.configure(serdeProps, false);
        final Serde<PurchasePattern> purchasePatternSerde =
                Serdes.serdeFrom(purchasePatternSerializer, purchasePatternDeserializer);

        KStream<String, PurchasePattern> purchasePatternKStream = purchaseKStream
                .mapValues(purchase -> PurchasePattern.builder(purchase).build());
        purchasePatternKStream.print(Printed.<String, PurchasePattern>toSysOut().withLabel("Purchase Pattern Transactions"));
        purchasePatternKStream.to("pattern", Produced.with(stringSerde, purchasePatternSerde));

        final Serializer<RewardAccumulator> rewardAccumulatorSerializer = new JsonSerializer<>();
        serdeProps.put("JsonClass", RewardAccumulator.class);
        rewardAccumulatorSerializer.configure(serdeProps, false);
        final Deserializer<RewardAccumulator> rewardAccumulatorDeserializer = new JsonDeserializer<>();
        serdeProps.put("JsonClass", RewardAccumulator.class);
        rewardAccumulatorDeserializer.configure(serdeProps, false);
        final Serde<RewardAccumulator> rewardAccumulatorSerde = Serdes.serdeFrom(rewardAccumulatorSerializer, rewardAccumulatorDeserializer);

        KStream<String, RewardAccumulator> rewardAccumulatorKStream = purchaseKStream
                .mapValues(purchase -> RewardAccumulator.builder(purchase).build());
        rewardAccumulatorKStream.print(Printed.<String, RewardAccumulator>toSysOut().withLabel("Reward Accumulator"));
        rewardAccumulatorKStream.to("reward", Produced.with(stringSerde, rewardAccumulatorSerde));

        KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(),streamsConfig);
        kafkaStreams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));






    }
}
