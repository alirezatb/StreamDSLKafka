package alireza.ch5;

import alireza.model.StockTransaction;
import alireza.model.TransactionSummary;
import alireza.serializer.JsonDeserializer;
import alireza.serializer.JsonSerializer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Windowing {
    public static void main(String[] args)
    {
        StreamsBuilder streamsBuilder= new StreamsBuilder();
        StreamsConfig streamsConfig = new StreamsConfig(props);

        Serde<String> stringSerde = new Serdes.StringSerde();


        Map<String, Object> serdeProps = new HashMap<>();
        Serializer<StockTransaction> stockTransactionSerializer = new JsonSerializer<>();
        serdeProps.put("JsonClass", stockTransactionSerializer);
        stockTransactionSerializer.configure(serdeProps, false);

        Deserializer<StockTransaction> stockTransactionDeserializer = new JsonDeserializer<>();
        serdeProps.put("JsonClass", stockTransactionDeserializer);
        stockTransactionDeserializer.configure(serdeProps, false);
        Serde<StockTransaction> stockTransactionSerde = Serdes.serdeFrom(stockTransactionSerializer, stockTransactionDeserializer);

        Serializer<TransactionSummary> transactionSummarySerializer = new JsonSerializer<>();
        serdeProps.put("JsonClass", transactionSummarySerializer);
        transactionSummarySerializer.configure(serdeProps, false);

        Deserializer<TransactionSummary> transactionSummaryDeserializer = new JsonDeserializer<>();
        serdeProps.put("JsonClass", transactionSummaryDeserializer);
        transactionSummaryDeserializer.configure(serdeProps, false);

        Serde<TransactionSummary> transactionSummarySerde = Serdes.serdeFrom(transactionSummarySerializer, transactionSummaryDeserializer);

        KTable<Windowed<TransactionSummary>, Long> trasactionSummaryKTable = streamsBuilder.stream("transactions", Consumed.with(stringSerde, stockTransactionSerde))
                .groupBy((k, v) -> TransactionSummary.from(v), Grouped.with(transactionSummarySerde, stockTransactionSerde))
                .windowedBy(SessionWindows.with(Duration.ofSeconds(20)).until(1000 * 60 * 15))
                .count();

        ///Join KTable and KStream
        KStream<String, TransactionSummary> countStream = trasactionSummaryKTable.toStream().map((k, v) -> {
            TransactionSummary transactionSummary = k.key();
            transactionSummary.setSummaryCount(v);
            String newKey = transactionSummary.getCustomerId();
            return new KeyValue<>(newKey, transactionSummary);
        });

        KTable<String, String> financialNews = streamsBuilder
                .table("finance", Consumed.with(Serdes.String(), Serdes.String()));

        ValueJoiner<TransactionSummary, String, String> NewsCountJoiner = (tsc, news) -> {
         return String.format("%d shares purchased %s related news [%s]",
                    tsc.getSummaryCount(), tsc.getStockTicker(), news);
        };

        KStream<String, String> joinedCountNews = countStream.leftJoin(financialNews, NewsCountJoiner,
                Joined.with(stringSerde, transactionSummarySerde, stringSerde)
                );










    }
}
