package org.osnormais.drive.api.infrastructure.commons;

public class ExceptionWrapper extends RuntimeException {

    private ExceptionWrapper(Throwable cause) {
        super(cause);
    }

    public static ExceptionWrapper wrap(Throwable cause) {
        return new ExceptionWrapper(cause);
    }

}
