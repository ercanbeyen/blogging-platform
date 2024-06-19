package com.ercanbeyen.bloggingplatform.dto.converter;

import com.ercanbeyen.bloggingplatform.dto.ApprovalDto;
import com.ercanbeyen.bloggingplatform.entity.Approval;
import org.springframework.stereotype.Component;

@Component
public record ApprovalDtoConverter() {
    public ApprovalDto convert(Approval approval) {
        return new ApprovalDto(
                approval.getId(),
                approval.getAuthorId(),
                approval.getTicket().getId()
        );
    }
}
