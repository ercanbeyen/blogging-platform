package com.ercanbeyen.bloggingplatform.constant.values;

import com.ercanbeyen.bloggingplatform.constant.messages.ResponseMessage;

public class TokenTime {
    public static final int ACCESS_TOKEN = 10 * 60 * 1000;
    public static final int REFRESH_TOKEN = 3 * ACCESS_TOKEN;
    public static final int CONFIRMATION_TOKEN = 15;

    private TokenTime() {
        throw new IllegalStateException(ResponseMessage.UTILITY_CLASSES_CANNOT_BE_INSTANTIATED);
    }
}
