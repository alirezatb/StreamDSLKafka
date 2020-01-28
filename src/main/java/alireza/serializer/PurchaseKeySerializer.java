package alireza.serializer;

import alireza.model.PurchaseKey;
import org.apache.commons.lang.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class PurchaseKeySerializer implements Serializer<PurchaseKey> {
    private String encoding = "UTF8";
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        String propertyName = isKey ? "key.serializer.encoding": "value.serializer.encoding";
        Object encodingValue = configs.get(propertyName);
        if (encodingValue == null)
            encodingValue = configs.get("serializer.encoding");
        if (encodingValue instanceof String)
            encoding = (String) encodingValue;
    }

    @Override
    public byte[] serialize(String topic, PurchaseKey purchaseKey) {
        try{
            if (purchaseKey == null){
                return null;
            }
            else
                return purchaseKey.getCustomerId().getBytes(encoding);
        }
        catch (UnsupportedEncodingException ex){
            throw new SerializationException("Error serializing purchase Key from String to byte [], due to unsupported serialization"+ encoding);
        }
        //return new byte[0];
    }

    @Override
    public void close() {

    }
}
