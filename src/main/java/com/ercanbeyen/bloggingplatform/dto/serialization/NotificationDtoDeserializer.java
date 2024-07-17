package com.ercanbeyen.bloggingplatform.dto.serialization;

import com.ercanbeyen.bloggingplatform.dto.NotificationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

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
            return objectMapper.readValue(new String(data, StandardCharsets.UTF_8), NotificationDto.class);
        } catch (Exception exception) {
            log.error("Unable to serialize object {}", data);
            return null;
        }
    }

    @Override
    public void close() {}
}
