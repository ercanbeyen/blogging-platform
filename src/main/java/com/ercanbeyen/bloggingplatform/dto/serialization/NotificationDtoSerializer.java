package com.ercanbeyen.bloggingplatform.dto.serialization;

import com.ercanbeyen.bloggingplatform.dto.NotificationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.commons.lang3.SerializationUtils;

import java.util.Map;

@Slf4j
public class NotificationDtoSerializer implements Serializer<NotificationDto> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public byte[] serialize(String topic, NotificationDto data) {
        log.info("In serialize");
        try {
            if (data == null) {
                log.warn("Null received at serializing");
                return null;
            }

            log.info("Serializing...");
           // byte[] temp = SerializationUtils.serialize(data);
            /*byte[] temp = objectMapper.writeValueAsBytes(data);
            return temp;*/
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception exception) {
            //throw new SerializationException("Error when serializing Notification to byte[]");
            log.error("Unable to deserialize message {}", data);
            return null;
        }
    }

    @Override
    public void close() {}
}
