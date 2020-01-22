package alireza.ch2.consumer;

import alireza.ch2.consumer.AbstractConsumerHandler;
import mykidong.domain.avro.events.Events;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.internals.Topic;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Properties;

public class TransactionalConsumer extends AbstractConsumerHandler<String, Events> {

    public TransactionalConsumer(Properties props, String topic){
        super(props, topic);
    }

    @Override
    public void run() {
        try
        {
            consumer.subscribe(Arrays.asList(topic),
                    new TransactionalConsumerRebalanceListener<>(this));
            consumer.poll(0);
            for(TopicPartition topicPartition: this.consumer.assignment()){
                long offsets = getOffsetsFromDB(groupId, topicPartition);
                consumer.seek(topicPartition, offsets);
            }
            while(true){
                if(this.wakedupCalled) {
                    throw new WakeupException();
                }
                ConsumerRecords<String, Events> records = consumer.poll(100);
                if(!records.isEmpty()){
                    for(ConsumerRecord<String, Events> record: records){
                        String key = record.key();
                        Events value = record.value();
                        processEvents(value);
                        saveEvents(value);
                        saveOffsetsToDB(groupId,record.topic(), record.partition(), record.offset());
                    }
                }
                commitDBTransaction();
                System.out.println("Commit Data into DB");
            }
        }
        catch (WakeupException ex)
        {

        }
        finally {
            commitDBTransaction();
            this.consumer.close();
        }

    }

}
