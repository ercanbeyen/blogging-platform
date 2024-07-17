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

    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            return false;
        }

        return status == ((UpdateTicketRequest) other).status;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
