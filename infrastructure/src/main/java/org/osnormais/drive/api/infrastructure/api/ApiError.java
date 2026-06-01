package org.osnormais.drive.api.infrastructure.api;

import java.util.List;

import org.osnormais.drive.api.application.exception.ApplicationException;
import org.osnormais.drive.api.domain.exception.DomainException;

public record ApiError(String message, List<String> errors) {

    public static ApiError with(String message) {
        return new ApiError(message, null);
    }

    public static ApiError from(final DomainException domainException) {
        return new ApiError(
                domainException.getMessage(),
                domainException.getErrors().stream().map(DomainException.Error::message).toList());
    }

    public static ApiError from(final ApplicationException applicationException) {
        return new ApiError(
                applicationException.getMessage(),
                applicationException.getErrors().stream().map(ApplicationException.Error::message).toList());
    }

}
