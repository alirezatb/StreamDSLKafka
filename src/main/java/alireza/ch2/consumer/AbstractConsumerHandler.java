package alireza.ch2.consumer;

import alireza.ch2.consumer.interfaces.ConsumerHandler;
import alireza.ch2.dac.ievents.IEvents;
import alireza.ch2.dac.mysql.MySQLEventsDao;
import mykidong.domain.avro.events.Events;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.Properties;

public abstract class AbstractConsumerHandler<K,V> implements ConsumerHandler<K, V> {
    protected String topic;
    protected int partition;
    protected String groupId;
    protected Properties config;
    protected KafkaConsumer<K,V> consumer;
    protected boolean wakedupCalled;

    private IEvents events;

    public AbstractConsumerHandler(Properties config, String topic, int partition){
        this.config = config;
        this.partition = partition;
        this.topic = topic;
        consumer = new KafkaConsumer<K, V>(config);
        groupId =config.getProperty(ConsumerConfig.GROUP_ID_CONFIG);
        events = new MySQLEventsDao();
    }

    public AbstractConsumerHandler(Properties props, String topic) {
        this(props, topic, -1);
    }

    public void processEvents(Events events)
    {
     // In here you can perform the stream processing on the stream
    }

    @Override
    public KafkaConsumer<K, V> getConsumer() {
        return consumer;
    }

    @Override
    public void setWakeupCalled(boolean WakedupCalled) {
        this.wakedupCalled = WakedupCalled;
    }

    public void saveOffsetsToDB(String groupID, String topic, int partition, long offsets){
        this.events.saveOffsetsToDB(groupID, topic, partition, offsets);
    }

    public long getOffsetsFromDB(String groupID, TopicPartition topicPartition){
        return this.events.getOffsetFromDB(groupID, topicPartition);
    }

    public void saveEvents(Events events){
        this.events.saveEventsToDB(events);
    }

    public void commitDBTransaction(){
        this.events.commitDBTransaction();
    }

    public abstract void run();
}
