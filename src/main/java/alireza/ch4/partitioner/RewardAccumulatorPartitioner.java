package alireza.ch4.partitioner;

import alireza.model.Purchase;
import org.apache.kafka.streams.processor.StreamPartitioner;

public class RewardAccumulatorPartitioner implements StreamPartitioner<String, Purchase> {

    @Override
    public Integer partition(String s, String key, Purchase value, int partition_number) {
        return value.getCustomerId().hashCode() % partition_number;
    }
}
