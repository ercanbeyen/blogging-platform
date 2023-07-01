package com.ercanbeyen.bloggingplatform.dto.serialization;

import com.ercanbeyen.bloggingplatform.dto.NotificationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

// public class NotificationDtoDeserializer implements Deserializer<NotificationDto> extends ErrorHandlingDeserializer<NotificationDto>
@Slf4j
public class NotificationDtoDeserializer implements Deserializer<NotificationDto> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public NotificationDto deserialize(String topic, byte[] data) {
        log.info("In deserialize");
        try {
            if (data == null) {
                log.warn("Null received at deserializing");
                return null;
            }

            log.info("Deserializing...");
            /*NotificationDto notificationDto = objectMapper.readValue(new String(data, StandardCharsets.UTF_8), NotificationDto.class);
            return notificationDto;*/
            return objectMapper.readValue(new String(data, StandardCharsets.UTF_8), NotificationDto.class);
            //return SerializationUtils.deserialize(data);

        } catch (Exception exception) {
            //throw new SerializationException("Error when deserializing byte[] to Notification");
            log.error("Unable to serialize object {}", data);
            return null;
        }
    }

    @Override
    public void close() {}
}
