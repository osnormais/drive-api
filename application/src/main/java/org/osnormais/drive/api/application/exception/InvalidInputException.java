package org.osnormais.drive.api.application.exception;

import java.util.List;

public class InvalidInputException extends SilentApplicationException {

    protected InvalidInputException(String message, List<Error> errors) {
        super(message, errors);
    }

    public static InvalidInputException with(String error) {
        return new InvalidInputException("Invalid input provided.", List.of(Error.with(error)));
    }

}
