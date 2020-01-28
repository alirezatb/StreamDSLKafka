package alireza.ch3.zmarket;

import alireza.model.Purchase;
import alireza.model.PurchasePattern;
import alireza.model.RewardAccumulator;
import alireza.serializer.JsonDeserializer;
import alireza.serializer.JsonSerializer;
import org.apache.kafka.common.serialization.*;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;

import java.util.HashMap;
import java.util.Map;

public class ZMartStreamAdvanced {
    public static void main(String[] args) {
        StreamsConfig config = new StreamsConfig(properties);
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        Map<String, Object> serdeProps = new HashMap<>();
        final Serializer<Purchase> purchaseSerializer = new JsonSerializer<>();
        serdeProps.put("JsonClass", purchaseSerializer);
        purchaseSerializer.configure(serdeProps, false);
        final Deserializer<Purchase> purchaseDeserializer = new JsonDeserializer<>();
        serdeProps.put("JsonClass", purchaseDeserializer);
        purchaseDeserializer.configure(serdeProps, false);

        Serde<String> stringSerde = Serdes.String();

        final Serde<Purchase> purchaseSerde =
                Serdes.serdeFrom(purchaseSerializer, purchaseDeserializer);

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
        ///////////////////////New Requirements///////////////////////////////////////////////////////
        KStream<String, Purchase> filteredpurchase = purchaseKStream.filter((purchaseKey, purchaseValue) -> purchaseValue.getPrice() > 5.00);

        Predicate<String, Purchase> isCoffee = (purchasekey, purchaseValue)-> purchaseValue.getDepartment().equalsIgnoreCase("coffee");
        Predicate<String, Purchase> isElectronic = (purchasekey, purchaseValue)-> purchaseValue.getDepartment().equalsIgnoreCase("electronic");
        KStream<String, Purchase> [] kstreamByDept = purchaseKStream.branch(isCoffee, isElectronic);

        kstreamByDept[0].to( "coffee", Produced.with(stringSerde, purchaseSerde));
        kstreamByDept[0].print(Printed.<String, Purchase>toSysOut().withLabel( "coffee"));

        kstreamByDept[1].to("electronics", Produced.with(stringSerde, purchaseSerde));
        kstreamByDept[1].print(Printed.<String, Purchase>toSysOut().withLabel("electronics"));






    }
}
