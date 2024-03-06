package com.databasecourse.erfan.web.error;

public final class UserDatabaseAlreadyExistException extends RuntimeException {

    private static final long serialVersionUID = 2261310537366287163L;

    public UserDatabaseAlreadyExistException() {
        super();
    }

    public UserDatabaseAlreadyExistException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserDatabaseAlreadyExistException(final String message) {
        super(message);
    }

    public UserDatabaseAlreadyExistException(final Throwable cause) {
        super(cause);
    }

}
