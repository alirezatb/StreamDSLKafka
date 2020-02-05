package alireza.ch4;

import alireza.ch4.joiner.PurchaseJoiner;
import alireza.model.CorrelatedPurchase;
import alireza.model.Purchase;
import alireza.model.RewardAccumulator;
import alireza.serializer.JsonDeserializer;
import alireza.serializer.JsonSerializer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class ZMartStreamJoiner {
    public static void main(String[] args) {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        StreamsConfig streamsConfig = new StreamsConfig(props);

        Serde<String> stringSerde = Serdes.String();

        Map<String, Object> serdeProps = new HashMap<>();
        final Serializer<Purchase> purchaseSerializer = new JsonSerializer<>();
        serdeProps.put("JsonClass", purchaseSerializer);
        purchaseSerializer.configure(serdeProps,false);

        final Deserializer<Purchase> purchaseDeserializer = new JsonDeserializer<>();
        serdeProps.put("JsonClass", purchaseDeserializer);
        purchaseDeserializer.configure(serdeProps, false);

        Serde<Purchase> purchaseSerde = Serdes.serdeFrom(purchaseSerializer, purchaseDeserializer);

        KeyValueMapper<String, Purchase, KeyValue<String, Purchase>> mappedKeyValue = (K, V) ->{
            Purchase maskedUserCreditCard = Purchase.builder(V).maskCreditCard().build();
            return new KeyValue<>(maskedUserCreditCard.getCustomerId(), maskedUserCreditCard);
        };

        KStream<String, Purchase> purchaseKStream = streamsBuilder.stream("transaction", Consumed.with(stringSerde, purchaseSerde))
                .map(mappedKeyValue);
        Predicate<String, Purchase> coffeePurchase = (key, purchase) -> purchase.getDepartment().equalsIgnoreCase("coffee");
        Predicate<String, Purchase> electronicPurchase = (key, purchase) -> purchase.getDepartment().equalsIgnoreCase("electronics");

        int COFFEE_PURCHASE = 0;
        int ELECTRONICS_PURCHASE = 1;

        KStream<String, Purchase> [] KeyedStreamBranched = purchaseKStream.selectKey((K,V) -> V.getCustomerId()).branch(coffeePurchase, electronicPurchase);

        KStream<String, Purchase> coffeeStream = KeyedStreamBranched[COFFEE_PURCHASE];
        KStream<String, Purchase> electronicsStream = KeyedStreamBranched[ELECTRONICS_PURCHASE];

        ValueJoiner<Purchase, Purchase, CorrelatedPurchase> purchaseValueJoiner = new PurchaseJoiner();
        JoinWindows differenceBetweenEvents = JoinWindows.of(Duration.ofMinutes(20));

        KStream<String, CorrelatedPurchase> joinedStream = coffeeStream.join(electronicsStream,
                    purchaseValueJoiner,
                    differenceBetweenEvents,
                    Joined.with(
                        stringSerde,
                        purchaseSerde,
                        purchaseSerde
                    ));

        KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), streamsConfig);
        kafkaStreams.start();

        kafkaStreams.close();






    }
}
