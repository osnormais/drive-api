package org.osnormais.drive.api.infrastructure.commons;

public class ExceptionWrapper extends RuntimeException {

    private ExceptionWrapper(final Throwable cause) {
        super(null, cause, true, false);
    }

    public static ExceptionWrapper wrap(final Throwable cause) {
        return new ExceptionWrapper(cause);
    }

}
