package com.ercanbeyen.bloggingplatform.util;

import com.ercanbeyen.bloggingplatform.constant.enums.RoleName;
import com.ercanbeyen.bloggingplatform.constant.messages.ResponseMessage;
import com.ercanbeyen.bloggingplatform.entity.Author;
import com.ercanbeyen.bloggingplatform.exception.data.DataForbidden;

public class RoleUtil {
    private static boolean isBanned(Author author) {
        return author.getRoles()
                .stream()
                .anyMatch(role -> role.getRoleName().equals(RoleName.BANNED));
    }

    public static void checkIsBanned(Author author) {
        if (isBanned(author)) {
            throw new DataForbidden(ResponseMessage.NOT_AUTHORIZED);
        }
    }
}
