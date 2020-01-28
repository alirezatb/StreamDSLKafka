package alireza.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;


import java.nio.charset.Charset;
import java.util.Map;

public class JsonSerializer<T> implements Serializer<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    public JsonSerializer(){
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, T data) {
        if(data == null)
            return null;
        try{
            return objectMapper.writeValueAsBytes(data);
        }
        catch (Exception ex)
        {
            throw new SerializationException("Exception in serializing data", ex);
        }
    }

    @Override
    public void close() {

    }
}
