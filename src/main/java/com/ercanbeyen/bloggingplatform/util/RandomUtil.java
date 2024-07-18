package com.ercanbeyen.bloggingplatform.util;

import java.util.UUID;

public class RandomUtil {
    private RandomUtil() {}

    public static String getRandomString() {
        return UUID.randomUUID().toString();
    }
}
