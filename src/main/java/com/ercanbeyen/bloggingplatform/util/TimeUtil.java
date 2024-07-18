package com.ercanbeyen.bloggingplatform.util;

import java.time.LocalDateTime;

public class TimeUtil {
    private TimeUtil() {}

    public static LocalDateTime calculateNow() {
        return LocalDateTime.now();
    }
}
