package com.ercanbeyen.bloggingplatform.util;

import com.ercanbeyen.bloggingplatform.constant.enums.RoleName;
import com.ercanbeyen.bloggingplatform.constant.messages.ResponseMessage;
import com.ercanbeyen.bloggingplatform.entity.Author;
import com.ercanbeyen.bloggingplatform.exception.data.DataForbidden;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    private SecurityUtil() {
        throw new IllegalStateException(ResponseMessage.UTILITY_CLASSES_CANNOT_BE_INSTANTIATED);
    }

    public static Author getLoggedInAuthor() {
        return (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static void checkAuthorAuthentication(String authorId) {
        if (!authorId.equals(getLoggedInAuthor().getId())) {
            throw new DataForbidden(ResponseMessage.NOT_AUTHORIZED);
        }
    }

    public static void checkAdminRole() {
        boolean isAdmin = doesAuthorHaveAuthority(RoleName.ADMIN);

        if (!isAdmin) {
            throw new DataForbidden(ResponseMessage.NOT_AUTHORIZED);
        }
    }

    public static void checkBannedRole() {
        boolean isBanned = doesAuthorHaveAuthority(RoleName.BANNED);

        if (isBanned) {
            throw new DataForbidden(ResponseMessage.NOT_AUTHORIZED);
        }
    }

    private static boolean doesAuthorHaveAuthority(RoleName roleName) {
        return getLoggedInAuthor()
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals(roleName.name()));
    }
}
