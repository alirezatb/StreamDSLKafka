package alireza.ch2.consumer;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;

public class TransactionalConsumerRebalanceListener<K,V> implements ConsumerRebalanceListener {
    AbstractIConsumerHandler<K,V> consumerHandler;
    public TransactionalConsumerRebalanceListener(AbstractIConsumerHandler<K,V> consumerHandler){
        this.consumerHandler = consumerHandler;
    }
    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> collection) {
        this.consumerHandler.commitDBTransaction();
    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> collection) {
        for(TopicPartition topicPartition: collection){
            String groupID = this.consumerHandler.groupId;
            long offsets = this.consumerHandler.getOffsetsFromDB(groupID, topicPartition);
            this.consumerHandler.getConsumer().seek(topicPartition, offsets);
        }
    }
}
