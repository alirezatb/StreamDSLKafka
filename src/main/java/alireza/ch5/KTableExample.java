package alireza.ch5;

import alireza.model.StockTickerData;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;

public class KTableExample {
    public static void main(String[] args) {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        StreamsConfig streamsConfig = new StreamsConfig();

        KTable<String, StockTickerData> tableStock = streamsBuilder.table("stock-ticker-table");
        KStream<String, StockTickerData> streamStock = streamsBuilder.stream("stock-ticker-stream");

    }
}
