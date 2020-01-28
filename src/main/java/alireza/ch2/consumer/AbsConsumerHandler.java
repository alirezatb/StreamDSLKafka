package alireza.ch2.consumer;

import alireza.ch2.consumer.interfaces.IConsumerHandler;
import alireza.ch2.dac.ievents.IEvents;
import alireza.ch2.dac.mysql.MySQLEventsDao;
import mykidong.domain.avro.events.Events;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.Properties;

public class AbsConsumerHandler<K,V> implements IConsumerHandler<K,V> {
    protected String topic;
    protected int partition;
    protected Properties config;
    protected KafkaConsumer<K,V> consumer;
    protected boolean wakeupCall;
    protected String groupId;
    protected IEvents events;


    public AbsConsumerHandler(Properties config, String topic, int partition){
        this.config = config;
        this.topic = topic;
        this.partition = partition;
        this.consumer = new KafkaConsumer<K, V>(config);
        this.groupId = config.getProperty(ConsumerConfig.GROUP_ID_CONFIG);
        this.events = new MySQLEventsDao();

    }
    public AbsConsumerHandler(Properties config, String topic){
        this(config, topic, -1);
    }

    @Override
    public KafkaConsumer<K, V> getConsumer() {
        return this.consumer;
    }

    @Override
    public void setWakeupCalled(boolean WakedupCalled) {
        this.wakeupCall = WakedupCalled;
    }

    public void saveEvents(Events events){
        this.events.saveEventsToDB(events);
    }
    public void saveOffsets(String groupID, String topic, int partition, long offset){
        this.events.saveOffsetsToDB(groupID, topic, partition, offset);
    }
    public long getOffsets(String groupID, TopicPartition topic_partition){
        return this.events.getOffsetFromDB(groupID, topic_partition);
    }
    public void commitTransaction(){
        this.events.commitDBTransaction();
    }

    @Override
    public void run() {

    }
}
