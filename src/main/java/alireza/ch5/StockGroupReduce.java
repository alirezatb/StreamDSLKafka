package alireza.ch5;

import alireza.model.ShareVolume;
import alireza.model.StockTransaction;
import alireza.model.TransactionSummary;
import alireza.serializer.JsonDeserializer;
import alireza.serializer.JsonSerializer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;

import java.util.HashMap;
import java.util.Map;

public class StockGroupReduce {
    public static void main(String[] args) {
        StreamsBuilder streamsBuilder= new StreamsBuilder();
        StreamsConfig streamsConfig= new StreamsConfig(props);

        Map<String, Object> serdeProps = new HashMap<>();

        Serializer<StockTransaction> stockTransactionSerializer = new JsonSerializer<>();
        serdeProps.put("JsonClass", stockTransactionSerializer);
        stockTransactionSerializer.configure(serdeProps, false);

        Deserializer<StockTransaction> stockTransactionDeserializer = new JsonDeserializer<>();
        serdeProps.put("JsonClass", stockTransactionDeserializer);
        stockTransactionDeserializer.configure(serdeProps, false);

        Serde<StockTransaction> stockTransactionSerde = Serdes.serdeFrom(stockTransactionSerializer, stockTransactionDeserializer);

        Serializer<ShareVolume> shareVolumeSerializer= new JsonSerializer<>();
        shareVolumeSerializer.configure(serdeProps, false);
        serdeProps.put("JsonClass", shareVolumeSerializer);

        Deserializer<ShareVolume> shareVolumeDeserializer= new JsonDeserializer<>();
        shareVolumeDeserializer.configure(serdeProps, false);
        serdeProps.put("JsonClass", shareVolumeDeserializer);

        Serde<ShareVolume> shareVolumeSerde =  Serdes.serdeFrom(shareVolumeSerializer, shareVolumeDeserializer);
        // notice regarding the usage of key/value pair in group by
        // you may not need to use the key/value in a group by if you are not changing the key.
        // for example, for the below record you have convert it

        KTable<String, ShareVolume> sumShareVolumes = streamsBuilder.stream("share-volume-topic", Consumed.with(Serdes.String(), stockTransactionSerde))
                .mapValues(transaction -> ShareVolume.builder(transaction).build())
                .groupBy((k, v) -> v.getSymbol(), Grouped.with(Serdes.String(), shareVolumeSerde))
                .reduce(ShareVolume::sum);






    }
}
