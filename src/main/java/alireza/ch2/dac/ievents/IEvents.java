package alireza.ch2.dac.ievents;

import mykidong.domain.avro.events.Events;
import org.apache.kafka.common.TopicPartition;

public interface IEvents {
    void saveEventsToDB(Events events);
    void saveOffsetsToDB(String groupId, String topic, int partition, long offset);
    void commitDBTransaction();
    long getOffsetFromDB(String groupId, TopicPartition topicPartition);
}
