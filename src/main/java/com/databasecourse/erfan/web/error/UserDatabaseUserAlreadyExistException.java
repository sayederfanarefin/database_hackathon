package com.databasecourse.erfan.web.error;

//UserAlreadyExistExceptionpackage com.databasecourse.erfan.web.error;

public final class UserDatabaseUserAlreadyExistException extends RuntimeException {

    private static final long serialVersionUID = 1861310522366287163L;

    public UserDatabaseUserAlreadyExistException() {
        super();
    }

    public UserDatabaseUserAlreadyExistException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserDatabaseUserAlreadyExistException(final String message) {
        super(message);
    }

    public UserDatabaseUserAlreadyExistException(final Throwable cause) {
        super(cause);
    }

}
