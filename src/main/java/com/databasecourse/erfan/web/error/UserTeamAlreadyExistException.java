package com.databasecourse.erfan.web.error;

public final class UserTeamAlreadyExistException extends RuntimeException {

    private static final long serialVersionUID = 3361310537366287163L;

    public UserTeamAlreadyExistException() {
        super();
    }

    public UserTeamAlreadyExistException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserTeamAlreadyExistException(final String message) {
        super(message);
    }

    public UserTeamAlreadyExistException(final Throwable cause) {
        super(cause);
    }

}
