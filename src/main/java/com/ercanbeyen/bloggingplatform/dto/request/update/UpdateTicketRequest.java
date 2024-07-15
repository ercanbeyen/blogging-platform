package com.ercanbeyen.bloggingplatform.dto.request.update;

import com.ercanbeyen.bloggingplatform.constant.enums.TicketStatus;
import com.ercanbeyen.bloggingplatform.dto.request.base.BaseTicketRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateTicketRequest extends BaseTicketRequest {
    private TicketStatus status;
}
