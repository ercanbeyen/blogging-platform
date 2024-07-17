package com.ercanbeyen.bloggingplatform.constant.values;

import com.ercanbeyen.bloggingplatform.constant.messages.ResponseMessage;

public class EntityName {
    public static final String AUTHOR = "Author";
    public static final String POST = "Post";
    public static final String COMMENT = "Comment";
    public static final String ROLE = "Role";
    public static final String NOTIFICATION = "Notification";
    public static final String CONFIRMATION_TOKEN = "Confirmation Token";
    public static final String TICKET = "Ticket";
    public static final String APPROVAL = "Approval";

    private EntityName() {
        throw new IllegalStateException(ResponseMessage.UTILITY_CLASSES_CANNOT_BE_INSTANTIATED);
    }
}
