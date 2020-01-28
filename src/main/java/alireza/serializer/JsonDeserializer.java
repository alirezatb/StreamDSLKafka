package alireza.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;


import java.util.Map;

public class JsonDeserializer<T> implements Deserializer<T> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Class<T> tClass;

    @SuppressWarnings("Unchecked")
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        tClass = (Class<T>) configs.get("JsonClass");

    }


    @Override
    public T deserialize(String topic, byte[] bytes) {
        if(bytes == null){
            return null;
        }
        T data;
        try{
          data = objectMapper.readValue(bytes,tClass);
        }
        catch (Exception e){
            throw new SerializationException("Exception Deserializing the objects", e);
        }
        return data;
    }

    @Override
    public void close() {

    }
}
