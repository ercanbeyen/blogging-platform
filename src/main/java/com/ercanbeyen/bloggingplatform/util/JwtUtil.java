package com.ercanbeyen.bloggingplatform.util;

import com.ercanbeyen.bloggingplatform.constant.messages.JwtMessage;
import com.ercanbeyen.bloggingplatform.constant.messages.ResponseMessage;

import java.util.Date;

public class JwtUtil {
    private JwtUtil() {
        throw new IllegalStateException(ResponseMessage.UTILITY_CLASSES_CANNOT_BE_INSTANTIATED);
    }

    public static boolean doesTokenNotExist(String authorizationHeader) {
        return authorizationHeader == null || !authorizationHeader.startsWith(JwtMessage.BEARER);
    }

    private static long calculateTokenTime(int validTime) {
        return System.currentTimeMillis() + validTime;
    }

    public static Date calculateExpirationDate(int validTime) {
        return new Date(calculateTokenTime(validTime));
    }
}
