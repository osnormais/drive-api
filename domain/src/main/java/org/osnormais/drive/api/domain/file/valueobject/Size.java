package org.osnormais.drive.api.domain.file.valueobject;

import org.osnormais.drive.api.domain.ValueObject;
import org.osnormais.drive.api.domain.validation.ValidationError;
import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

public record Size(long bytes) implements ValueObject {

    public static Size of(final long bytes) {
        return new Size(bytes);
    }

    @Override
    public void validate(final ValidationHandler handler) {

        if (bytes <= 0)
            handler.append(ValidationError.with("Size.bytes must be greater than 0"));

    }

}
