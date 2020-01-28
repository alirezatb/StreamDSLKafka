package alireza.ch2.producer;

import alireza.ch2.partitioner.PurchaseKeyPartitioner;
import alireza.model.PurchaseKey;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import mykidong.domain.avro.events.Events;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.*;
import java.util.concurrent.Future;

public class TransactionalProducer {
    public  TransactionalProducer(){}

    public static void main(String[] args) {
        Properties config = new Properties();
        String registry = System.getProperty("registry", "http://localhost:8081");
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"alireza:9092");
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        config.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "purchase-transaction");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "alireza.serializer.PurchaseKeySerializer");
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "io.confluent.kafka.serializers.KafkaAvroSerializer");
        config.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        config.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,
                "alireza.ch2.partitioner.PurchaseKeyPartitioner");
        config.put(ProducerConfig.ACKS_CONFIG, "all");
        config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "1");
        config.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
        config.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, registry);


        KafkaProducer<PurchaseKey, Events> producer = new KafkaProducer<PurchaseKey, Events>(config);
        List<String> lstItems = new ArrayList<String>(Arrays.asList("BMW 330e", "Mercedes CLA", "Range Rover Velar",
                "Porsche Cayan", "Lexsus RX"));
        producer.initTransactions();
        try
        {
            while(true){
                producer.beginTransaction();
                double customer_id = (Math.random() * ((10 - 1) + 1)) + 1;
                double randomLuxuryOrder = (Math.random() * ((4 - 0) + 0)) + 0;
                Date date = new Date();
                PurchaseKey purchaseInfo = new PurchaseKey(String.valueOf(customer_id), new Date());
                Events events = new Events();
                events.setCustomerId(String.valueOf(customer_id));
                events.setEventTime(date.getTime());
                events.setOrderInfo(lstItems.get((int)randomLuxuryOrder));
                Future<RecordMetadata> response = producer.send(new ProducerRecord<PurchaseKey, Events>("user-transaction", purchaseInfo, events));
                RecordMetadata recordMetadata = response.get();
                System.out.println(recordMetadata.topic() +" "+ recordMetadata.partition() +" "+ recordMetadata.offset());
                //ProducerRecord<PurchaseKey, Events> record = new ProducerRecord<PurchaseKey, Events>("user-transaction", purchaseInfo, events);

//                producer.send(record, new Callback() {
//                    @Override
//                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
//                        if (e != null){
//                            e.printStackTrace();
//                        }
//                        System.out.println("System partition and offset committed"+
//                                recordMetadata.partition() +" "+ recordMetadata.offset());
//
//                    }
//                });
                producer.commitTransaction();
            }
        }
        catch (Exception ex){
            producer.abortTransaction();

        }
        producer.close();




    }

}
