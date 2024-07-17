package com.ercanbeyen.bloggingplatform.constant.messages;

public class NotificationMessage {
    public static final String POST_NOTIFICATION = "post-notification";
    public static final String COMMENT_NOTIFICATION = "comment-notification";

    private NotificationMessage() {
        throw new IllegalStateException(ResponseMessage.UTILITY_CLASSES_CANNOT_BE_INSTANTIATED);
    }
}
