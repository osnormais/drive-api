package org.osnormais.drive.api.domain.file.valueobject;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.time.Instant;
import java.util.Optional;

import org.osnormais.drive.api.domain.ValueObject;
import org.osnormais.drive.api.domain.validation.ValidationError;
import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

public record Publication(
        Instant publishedAt,
        Publication.Status status,
        Optional<Publication.Error> error) implements ValueObject {

    public static Publication success() {
        return new Publication(Instant.now(), Status.SUCCESS, Optional.empty());
    }

    public static Publication error(final String message) {
        return new Publication(null, Status.ERROR, Optional.of(new Publication.Error(message)));
    }

    public static Publication pending() {
        return new Publication(null, Status.PENDING, Optional.empty());
    }

    @Override
    public void validate(final ValidationHandler handler) {

        if (isNull(publishedAt) && Status.SUCCESS.equals(status))
            handler.append(new ValidationError("Publication.publishedAt should not be null"));

        if (isNull(status))
            handler.append(new ValidationError("Publication.status should not be null"));

        if (nonNull(error))
            error.ifPresent(e -> e.validate(handler));

        if ((isNull(error) || error.isEmpty()) && Status.ERROR.equals(status))
            handler.append(new ValidationError("Publication.error should not be null when status is ERROR"));

        if (nonNull(error) && error.isPresent() && Status.SUCCESS.equals(status))
            handler.append(new ValidationError("Publication.error should be empty when status is SUCCESS"));

    }

    public enum Status {
        PENDING, SUCCESS, ERROR
    }

    public record Error(String message) implements ValueObject {

        public Error {
            if (isNull(message) || message.isBlank())
                message = "Publication error";
        }

        @Override
        public void validate(final ValidationHandler handler) {

            if (isNull(message))
                handler.append(new ValidationError("Publication.Error.message should not be null"));

            if (nonNull(message) && message.isBlank())
                handler.append(new ValidationError("Publication.Error.message should not be blank"));

        }

    }

}
