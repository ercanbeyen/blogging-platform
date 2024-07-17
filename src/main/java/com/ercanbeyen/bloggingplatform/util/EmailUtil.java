package com.ercanbeyen.bloggingplatform.util;

import com.ercanbeyen.bloggingplatform.constant.messages.ResponseMessage;

public class EmailUtil {
    public static final String SUPPORT_EMAIL = "support@bloggingplatform.com";

    private EmailUtil() {
        throw new IllegalStateException(ResponseMessage.UTILITY_CLASSES_CANNOT_BE_INSTANTIATED);
    }
}
