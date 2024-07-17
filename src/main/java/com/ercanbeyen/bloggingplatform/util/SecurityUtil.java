package com.ercanbeyen.bloggingplatform.util;

import com.ercanbeyen.bloggingplatform.constant.enums.RoleName;
import com.ercanbeyen.bloggingplatform.constant.messages.ResponseMessage;
import com.ercanbeyen.bloggingplatform.entity.Author;
import com.ercanbeyen.bloggingplatform.entity.Role;
import com.ercanbeyen.bloggingplatform.exception.data.DataForbidden;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    private SecurityUtil() {
        throw new IllegalStateException(ResponseMessage.UTILITY_CLASSES_CANNOT_BE_INSTANTIATED);
    }

    public static Author getLoggedInAuthor() {
        return (Author) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static void checkAdminRole() {
        boolean isAdmin = getLoggedInAuthor()
                .getRoles()
                .stream()
                .map(Role::getRoleName)
                .anyMatch(roleName -> roleName == RoleName.ADMIN);

        if (!isAdmin) {
            throw new DataForbidden(ResponseMessage.NOT_AUTHORIZED);
        }
    }
}
