package alireza.ch5;

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
import org.apache.kafka.streams.kstream.KeyValueMapper;

import java.util.HashMap;
import java.util.Map;

public class GlobaKTable {
    public static void main(String[] args) {
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

        KeyValueMapper<>
    }
}
