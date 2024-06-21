package com.ercanbeyen.bloggingplatform.util;

import com.ercanbeyen.bloggingplatform.entity.Author;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static Author getLoggedInAuthor() {
        return (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
