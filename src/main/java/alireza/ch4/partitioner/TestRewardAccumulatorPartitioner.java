package alireza.ch4.partitioner;

import alireza.model.Purchase;
import org.apache.kafka.streams.processor.StreamPartitioner;

public class TestRewardAccumulatorPartitioner implements StreamPartitioner<String, Purchase> {
    @Override
    public Integer partition(String s, String key, Purchase purchase, int numPartitions) {
        return purchase.getCustomerId().hashCode() % numPartitions;
    }
}
