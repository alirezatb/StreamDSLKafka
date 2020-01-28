package alireza.ch2.consumer;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;

public class ConsumerRebalancer<K,V> implements ConsumerRebalanceListener {
    AbsConsumerHandler<K,V> abstractConsumerHandler;
    public ConsumerRebalancer(AbsConsumerHandler<K,V> abstractConsumerHandler){
        this.abstractConsumerHandler = abstractConsumerHandler;
    }
    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> collection) {
        abstractConsumerHandler.commitTransaction();
    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> collection) {
        for(TopicPartition topic_partition: collection){
            String groupID = abstractConsumerHandler.groupId;
            long offsets = abstractConsumerHandler.getOffsets(groupID, topic_partition);
            abstractConsumerHandler.getConsumer().seek(topic_partition, offsets);

        }
    }
}
