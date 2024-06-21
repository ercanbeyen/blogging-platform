package com.ercanbeyen.bloggingplatform.dto.converter;

import com.ercanbeyen.bloggingplatform.entity.ConfirmationToken;
import com.ercanbeyen.bloggingplatform.dto.ConfirmationTokenDto;
import org.springframework.stereotype.Component;

@Component
public class ConfirmationTokenDtoConverter {
    public ConfirmationTokenDto convert(ConfirmationToken confirmationToken) {
        return ConfirmationTokenDto.builder()
                .id(confirmationToken.getId())
                .authorId(confirmationToken.getAuthorId())
                .token(confirmationToken.getToken())
                .createdAt(confirmationToken.getConfirmedAt())
                .expiresAt(confirmationToken.getExpiresAt())
                .confirmedAt(confirmationToken.getConfirmedAt())
                .build();
    }
}
