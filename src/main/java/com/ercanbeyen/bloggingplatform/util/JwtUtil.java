package com.ercanbeyen.bloggingplatform.util;

import com.ercanbeyen.bloggingplatform.constant.messages.JwtMessage;

import java.util.Date;

public class JwtUtil {
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
