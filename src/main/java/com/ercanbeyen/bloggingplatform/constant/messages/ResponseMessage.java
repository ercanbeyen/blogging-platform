package com.ercanbeyen.bloggingplatform.constant.messages;

public class ResponseMessage {
    public static final String NOT_FOUND = "%s %s is not found";
    public static final String NOT_AUTHORIZED = "You are not authorized";
    public static final String SUCCESS = "%s %s is successfully %s";
    public static final String ALREADY_CONFIRMED = "Account already confirmed";
    public static final String INVALID_EMAIL_FORMAT = "Invalid email format";
    public static final String SHOULD_NOT_BLANK = " should not be blank";
    public static final String UTILITY_CLASSES_CANNOT_BE_INSTANTIATED = "Utility classes cannot be instantiated";

    private ResponseMessage() {
        throw new IllegalStateException(UTILITY_CLASSES_CANNOT_BE_INSTANTIATED);
    }

    public static class Operation {
        public static final String CREATED = "created";
        public static final String UPDATED = "updated";
        public static final String DELETED = "deleted";

        private Operation() {
            throw new IllegalStateException(UTILITY_CLASSES_CANNOT_BE_INSTANTIATED);
        }
    }

    public static class State {
        public static final String NEW = "New";

        private State() {
            throw new IllegalStateException(UTILITY_CLASSES_CANNOT_BE_INSTANTIATED);
        }
    }
}
