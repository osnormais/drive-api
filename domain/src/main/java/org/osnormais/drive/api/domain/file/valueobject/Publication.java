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

    @Override
    public void validate(final ValidationHandler handler) {

        if (isNull(publishedAt))
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
        SUCCESS, ERROR
    }

    public record Error(String message) implements ValueObject {

        @Override
        public void validate(final ValidationHandler handler) {

            if (isNull(message))
                handler.append(new ValidationError("Publication.Error.message should not be null"));

            if (nonNull(message) && message.isBlank())
                handler.append(new ValidationError("Publication.Error.message should not be blank"));

        }

    }

}
